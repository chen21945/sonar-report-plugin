package org.sonarsource.plugins.report.constant;

/**
 * 配置
 */
public class ReportConfig {

    public static final String ENABLED = "sonar.report.pdf.enabled";

    public static final String USERNAME = "sonar.report.pdf.username";

    public static final String PASSWORD = "sonar.report.pdf.password";

    public static final String FILE_TYPE = "sonar.report.type";

    public class WSConfig {

        public static final String HOST_URL = "http://192.168.56.101:9000/";
        /*报表接口*/
        public static final String API_REPORTS = "api/reports";
        /*获取组件接口（项目信息）*/
        public static final String API_COMPONENTS_SHOW = "api/components/show";
    }
}
