package org.sonarsource.plugins.report.constant;

import org.apache.commons.lang3.StringUtils;

public enum MetricKeys {

    NCLOC("ncloc", ReportTexts.GENERAL_LINES_OF_CODE),
    CLASSES("classes", ReportTexts.GENERAL_CLASSES),
    FUNCTIONS("functions", ReportTexts.GENERAL_METHODS),
    DICTIONARIES("directories", ReportTexts.GENERAL_DICTIONARIES),
    COMPLEXITY("complexity", ReportTexts.GENERAL_COMPLEXITY),
    COGNITIVE_COMPLEXITY("cognitive_complexity", ReportTexts.GENERAL_COGNITIVE_COMPLEXITY),
    COMMENT_LINES_DENSITY("comment_lines_density", ReportTexts.GENERAL_COMMENTS),
    COMMENT_LINES("comment_lines", ReportTexts.GENERAL_COMMENT_LINES),

    ALERT_STATUS("alert_status", ReportTexts.GENERAL_ALERT_STATUS),
    QUALITY_GATE_DETAILS("quality_gate_details", ReportTexts.GENERAL_ALERT_STATUS),

    BUGS("bugs", ReportTexts.GENERAL_BUGS),
    SECURITY("vulnerabilities", ReportTexts.GENERAL_SECURITY),
    MAINTAINABILITY("code_smells", ReportTexts.GENERAL_MAINTAINABILITY),
    TECHNICAL_DEBT("sqale_index", ReportTexts.GENERAL_TECHNICAL_DEBT),
    COVERAGE("coverage", ReportTexts.GENERAL_COVERAGE),
    DUPLICATE_LINE_DENSITY("duplicated_lines_density", ReportTexts.GENERAL_DUPLICATED_LINES_DENSITY),
    DUPLICATED_BLOCKS("duplicated_blocks", ReportTexts.GENERAL_DUPLICATED_BLOCKS),

    BLOCKER_VIOLATIONS("blocker_violations", ReportTexts.GENERAL_BLOCKER_VIOLATIONS),
    CRITICAL_VIOLATIONS("critical_violations", ReportTexts.GENERAL_CRITICAL_VIOLATIONS),
    MAJOR_VIOLATIONS("major_violations", ReportTexts.GENERAL_MAJOR_VIOLATIONS),
    MINOR_VIOLATIONS("minor_violations", ReportTexts.GENERAL_MINOR_VIOLATIONS),
    INFO_VIOLATIONS("info_violations", ReportTexts.GENERAL_INFO_VIOLATIONS);

    private String key;
    private String desc;

    public String getKey() {
        return key;
    }

    public String getDesc() {
        return desc;
    }

    MetricKeys(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static MetricKeys get(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        for (MetricKeys metricKey : MetricKeys.values()) {
            if (metricKey.getKey().equals(key)) {
                return metricKey;
            }
        }
        return null;
    }

}
