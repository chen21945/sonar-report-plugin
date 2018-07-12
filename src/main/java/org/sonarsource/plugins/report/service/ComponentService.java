package org.sonarsource.plugins.report.service;

import org.apache.commons.lang3.StringUtils;
import org.sonarsource.plugins.report.model.Project;

import java.io.IOException;
import java.util.Date;

/**
 * Component获取服务
 */
public class ComponentService extends BaseService {

    public Project getProjcet(String projectKey) throws IOException {
        if (StringUtils.isBlank(projectKey)) {
            return null;
        }
//        String url = getUrl(ReportConfig.WSConfig.API_COMPONENTS_SHOW,
//                ImmutableMap.of("component", projectKey));
//        RequestManager manager = RequestManager.getInstance();
//        String result = manager.get(url);
//        JSONObject object = JSON.parseObject(result);
//        Object component = object.get("component");
//        return JSON.parseObject(JSON.toJSONString(component), Project.class);
        Project project = new Project();
        project.setId("1231232");
        project.setName("LRC");
        project.setKey("SVW.LRC");
        project.setVersion("1.1");
        project.setAnalysisDate(new Date());
        return project;
    }


    public ComponentService(String baseUrl) {
        super.baseUrl = baseUrl;
    }

    public ComponentService() {

    }


}
