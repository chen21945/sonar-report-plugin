package org.sonarsource.plugins.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Rule {
    private String key;
    private String name;
    private String status;
    private String langName;
    private String lang;
    private Integer count;
}
