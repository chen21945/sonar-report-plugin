package org.sonarsource.plugins.report.constant;

public enum MetricKeys {

    PROFILE("quality_profiles"),
    DUPLICATED_LINES("duplicated_lines"),
    DUPLICATED_BLOCKS("duplicated_blocks"),
    DUPLICATED_FILES("duplicated_files"),
    CLASSES("classes"),
    COMMENT_LINES("comment_lines"),
    COMPLEXITY("complexity"),
    FUNCTIONS("functions"),
    NCLOC("ncloc"),
    DIRECTORIES("directories"),
    COVERAGE("coverage"),
    TEST_EXECUTION_TIME("test_execution_time"),
    SKIPPED_TESTS("skipped_tests"),
    TESTS("tests"),
    TEST_ERRORS("test_errors"),
    TEST_FAILURES("test_failures"),
    TEST_SUCCESS_DENSITY("test_success_density"),
    VIOLATIONS("violations"),
    SECURITY("vulnerabilities"),
    BUGS("bugs"),
    MAINTAINABILITY("code_smells"),
    FILE_COMPLEXITY_DISTRIBUTION("file_complexity_distribution"),
    DUPLICATED_LINES_DENSITY("duplicated_lines_density"),
    CLASS_COMPLEXITY("class_complexity"),
    FUNCTION_COMPLEXITY("function_complexity"),
    COMMENT_LINES_DENSITY("comment_lines_density"),
    TECHNICAL_DEBT("sqale_index"),
    RELIABILITY_REMEDIATION_EFFORT("reliability_remediation_effort"),
    SECUTITY_REMEDIATION_EFFORT("security_remediation_effort"),
    BLOCKER_VIOLATIONS("blocker_violations"),
    CRITICAL_VIOLATIONS("critical_violations"),
    MAJOR_VIOLATIONS("major_violations"),
    MINOR_VIOLATIONS("minor_violations"),
    INFO_VIOLATIONS("info_violations");

    private String key;

    MetricKeys(String key) {
        this.key = key;
    }
}
