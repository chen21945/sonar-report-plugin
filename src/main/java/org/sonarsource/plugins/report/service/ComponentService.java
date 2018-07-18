package org.sonarsource.plugins.report.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sonarsource.plugins.report.constant.ReportConfig;
import org.sonarsource.plugins.report.dto.AnalysisDto;
import org.sonarsource.plugins.report.dto.IssueDto;
import org.sonarsource.plugins.report.model.Analysis;
import org.sonarsource.plugins.report.model.Component;
import org.sonarsource.plugins.report.model.Measure;
import org.sonarsource.plugins.report.model.Project;
import org.sonarsource.plugins.report.support.RequestManager;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Component获取服务
 */
@Slf4j
public class ComponentService extends BaseService {

    /**
     * 获取项目信息
     *
     * @param projectKey
     * @return
     * @throws IOException
     */
    public Project getProjcet(String projectKey) {
        if (StringUtils.isBlank(projectKey)) {
            return null;
        }
        String url = getUrl(ReportConfig.WSConfig.API_COMPONENTS_SHOW,
                ImmutableMap.of("component", projectKey));
        RequestManager manager = RequestManager.getInstance();
        try {
            String result = manager.get(url);
            JSONObject object = JSON.parseObject(result);
            Object component = object.get("component");
            return JSON.parseObject(JSON.toJSONString(component), Project.class);
        } catch (IOException e) {
            throw new ReportException("error get project with key [{" + projectKey + "}]", e);
        }
    }


    /**
     * 项目分析结果
     *
     * @param projectKey
     * @return
     */
    public List<Analysis> getAnalysis(String projectKey, int pageIndex, int pageSize) {
        if (StringUtils.isBlank(projectKey)) {
            return Collections.emptyList();
        }
        //取最近的一次分析
        String url = getUrl(ReportConfig.WSConfig.API_ANALYSIS_SEARCH,
                ImmutableMap.of("project", projectKey,
                        "p", pageIndex,
                        "ps", pageSize));
        AnalysisDto object;
        try {
            String result = RequestManager.getInstance().get(url);
            object = JSON.parseObject(result, AnalysisDto.class);
        } catch (IOException e) {
            throw new ReportException("error get analysis with key [{" + projectKey + "}]", e);
        }

        return object != null ? object.getAnalyses() : Collections.emptyList();
    }

    /**
     * 指标
     *
     * @param projectKey
     * @return
     */
    public List<Measure> getMeasures(String projectKey, List<String> metrics) {
        if (StringUtils.isBlank(projectKey)) {
            return Collections.emptyList();
        }
        String metricsStr = metrics.stream().collect(Collectors.joining(","));
        String url = getUrl(ReportConfig.WSConfig.API_MEASURES,
                ImmutableMap.of("component", projectKey,
                        "metricKeys", metricsStr));
        JSONObject jsonObject;
        try {
            String result = RequestManager.getInstance().get(url);
            jsonObject = JSON.parseObject(result, JSONObject.class);
        } catch (IOException e) {
            throw new ReportException("error get measures with key [{" + projectKey + "}]", e);
        }
        if (jsonObject != null) {
            Component component = JSON.parseObject(JSON.toJSONString(jsonObject.get("component")), Component.class);
            if (component != null) {
                return component.getMeasures();
            }
        }
        return Collections.emptyList();
    }

    public IssueDto getIssues(String projectKey, List<String> facets, List<String> types, int pageSize, int pageIndex) {
        if (StringUtils.isBlank(projectKey)) {
            return null;
        }
        String facetStr = facets.stream().collect(Collectors.joining(","));
        String typeStr = types.stream().collect(Collectors.joining(","));
        Map<String, Object> params = new HashMap<>();
        params.put("componentKeys", projectKey);
        params.put("resolved", false);
        params.put("p", pageIndex);
        params.put("ps", pageSize);
        params.put("facets", facetStr);
        params.put("s", "SEVERITY");
        params.put("asc", false);
        params.put("types", typeStr);
        String url = getUrl(ReportConfig.WSConfig.API_ISSUES_SEARCH, params);

        try {
            String result = RequestManager.getInstance().get(url);
            return JSON.parseObject(result, IssueDto.class);
        } catch (IOException e) {
            throw new ReportException("error get issues with key [{" + projectKey + "}]", e);
        }
    }


    public ComponentService(String baseUrl) {
        super.baseUrl = baseUrl;
    }

    private ComponentService() {

    }

    private static ComponentService service = null;

    public static synchronized ComponentService getInstance() {
        if (service == null) {
            service = new ComponentService();
        }
        return service;
    }


}