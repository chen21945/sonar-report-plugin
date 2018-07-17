package org.sonarsource.plugins.report.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class QualityGateDetail {

    private String level;

    private List<QualityGateCondition> conditions;

    @Getter
    @Setter
    public class QualityGateCondition {
        private String metric;

        private String op;

        private String warning;

        private String error;

        private String actual;

        private String level;
    }
}

