package org.sonarsource.plugins.report.constant;

/**
 * 配置
 */
public class ReportConfig {

    public static final String ENABLED = "sonar.report.pdf.enabled";

    public static final String SONAR_HOST_URL = "sonar.host.url";

    public static final String USERNAME = "sonar.report.pdf.username";

    public static final String PASSWORD = "sonar.report.pdf.password";

    public static final String FILE_TYPE = "sonar.report.type";

    public class WSConfig{
        public static final String CONTROLLER_REPORT = "api/reports";
    }
}
