package org.sonarsource.plugins.report.model;

import lombok.Data;

import java.util.List;


@Data
public class Measure {

    private String metric;

    private String value;

    private List<Period> periods;


    @Data
    class Period {

        private Integer index;

        private String value;
    }
}
