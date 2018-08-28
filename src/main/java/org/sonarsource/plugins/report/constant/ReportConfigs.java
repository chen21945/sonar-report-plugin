package org.sonarsource.plugins.report.constant;

import java.util.Arrays;
import java.util.List;

public class ReportConfigs {

    public static final String GLOBAL_ENABLED = "sonar.report.global.enabled";

    public static final String ENABLED = "sonar.report.enabled";

    public static final String ISSUE_TYPES = "sonar.report.issueTypes";

    public static final String SEVERITY_TYPES = "sonar.report.severity";

    public static final String NEW_ISSUE = "sonar.report.newIssue";

    public static final String TO_EMAILS = "sonar.report.emails";


    /**
     * 获取配置项
     *
     * @return
     */
    public static List<String> getConfigKeys() {
        return Arrays.asList(GLOBAL_ENABLED, ENABLED, ISSUE_TYPES, SEVERITY_TYPES, NEW_ISSUE, TO_EMAILS);
    }

}
