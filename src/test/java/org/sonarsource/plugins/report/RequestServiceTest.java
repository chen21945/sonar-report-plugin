package org.sonarsource.plugins.report;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonarsource.plugins.report.model.Analysis;
import org.sonarsource.plugins.report.model.Measure;
import org.sonarsource.plugins.report.model.Project;
import org.sonarsource.plugins.report.service.BaseService;
import org.sonarsource.plugins.report.service.ComponentService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestServiceTest {


    @Mock
    private Project project;

//    private String baseUrl = "http://192.168.56.101:9000/";
    private String baseUrl = "http://test.sonarqube.csvw.com/";

    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        when(project.getId()).thenReturn("AWRfSvAMoUcKZGf-I1aE");
        when(project.getName()).thenReturn("SVW.LRC");
        when(project.getKey()).thenReturn("SVW.LRC");
    }

    @Test
    public void getProjectTest() {
        ComponentService service = new ComponentService(baseUrl);
        Project project = service.getProjcet("SVW.LRC");
        System.out.println(project.toString());
    }

    @Test
    public void getAnalysisTest() {
        ComponentService service = new ComponentService(baseUrl);
        List<Analysis> analyses = service.getAnalysis("SVW.LRC",1,1);
        System.out.println(JSON.toJSONString(analyses));
    }

    @Test
    public void getMeasuresTest(){
        ComponentService service = new ComponentService(baseUrl);
        List<Measure> measures = service.getMeasures("SVW.LRC", Arrays.asList("ncloc","bugs","complexity"));
        System.out.println(JSON.toJSONString(measures));
    }

    @Test
    public void getURLTest() {
        Map<String, Object> map = ImmutableMap.of(
                "a", 1,
                "b", 2,
                "c", 3
        );
        BaseService baseService = new BaseService();
        System.out.println(baseService.getUrl("api/search", map));
    }
}
