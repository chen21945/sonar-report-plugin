package org.sonarsource.plugins.report.constant;

public enum MetricKeys {

    NCLOC("ncloc", ReportTexts.GENERAL_LINES_OF_CODE),
    CLASSES("classes", ReportTexts.GENERAL_CLASSES),
    FUNCTIONS("functions", ReportTexts.GENERAL_METHODS),
    DICTIONARIES("directories", ReportTexts.GENERAL_DICTIONARIES),
    COMPLEXITY("complexity", ReportTexts.GENERAL_COMPLEXITY),
    COGNITIVE_COMPLEXITY("cognitive_complexity", ReportTexts.GENERAL_COGNITIVE_COMPLEXITY),
    COMMENT_LINES_DENSITY("comment_lines_density", ReportTexts.GENERAL_COMMENTS),
    COMMENT_LINES("comment_lines", ReportTexts.GENERAL_COMMENT_LINES),

//    DIRECTORIES("directories"),
//    DUPLICATED_LINES("duplicated_lines"),
//    DUPLICATED_BLOCKS("duplicated_blocks"),
//    DUPLICATED_FILES("duplicated_files"),
//
//    PROFILE("quality_profiles"),
//
//    COVERAGE("coverage"),
//    TEST_EXECUTION_TIME("test_execution_time"),
//    SKIPPED_TESTS("skipped_tests"),
//    TESTS("tests"),
//    TEST_ERRORS("test_errors"),
//    TEST_FAILURES("test_failures"),
//    TEST_SUCCESS_DENSITY("test_success_density"),
//    VIOLATIONS("violations"),
//    SECURITY("vulnerabilities"),
//    BUGS("bugs"),
//    MAINTAINABILITY("code_smells"),
//    FILE_COMPLEXITY_DISTRIBUTION("file_complexity_distribution"),
//    DUPLICATED_LINES_DENSITY("duplicated_lines_density"),
//
//
//    TECHNICAL_DEBT("sqale_index"),
//    RELIABILITY_REMEDIATION_EFFORT("reliability_remediation_effort"),
//    SECUTITY_REMEDIATION_EFFORT("security_remediation_effort"),
//    BLOCKER_VIOLATIONS("blocker_violations"),
//    CRITICAL_VIOLATIONS("critical_violations"),
//    MAJOR_VIOLATIONS("major_violations"),
//    MINOR_VIOLATIONS("minor_violations"),
//    INFO_VIOLATIONS("info_violations")

//    CLASS_COMPLEXITY("class_complexity",ReportTexts.GENERAL_PER_CLASS),
//    FUNCTION_COMPLEXITY("function_complexity",ReportTexts.GENERAL_DECISION_POINTS),
    ;

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

}
