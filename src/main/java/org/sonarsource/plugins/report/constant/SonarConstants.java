package org.sonarsource.plugins.report.constant;

public class SonarConstants {

    public static final String QUALITY_GATE_OK = "OK";

    public static final String QUALITY_GATE_ERROR = "ERROR";

    public enum Severity {
        INFO("INFO"),
        MINOR("MINOR"),
        MAJOR("MAJOR"),
        CRITICAL("CRITICAL"),
        BLOCKER("BLOCKER");

        private String key;

        Severity(String key) {
            this.key = key;
        }
    }

    public enum IssueType {
        BUG("BUG"),
        VULNERABILITY("VULNERABILITY"),
        CODE_SMELL("CODE_SMELL");

        private String key;

        IssueType(String key) {
            this.key = key;
        }

    }

}
