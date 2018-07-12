package org.sonarsource.plugins.report;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.sonarsource.plugins.report.model.Project;
import org.sonarsource.plugins.report.service.BaseService;
import org.sonarsource.plugins.report.service.ComponentService;

import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestServiceTest {


    @Mock
    private Project project;


    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        when(project.getId()).thenReturn("AWRfSvAMoUcKZGf-I1aE");
        when(project.getName()).thenReturn("SVW.LRC");
        when(project.getKey()).thenReturn("SVW.LRC");
    }

    @Test
    public void getProjectTest() throws Exception {
        ComponentService service = new ComponentService("http://192.168.56.101:9000/");
        Project project = service.getProjcet("SVW.LRC");
        System.out.println(project.toString());
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
