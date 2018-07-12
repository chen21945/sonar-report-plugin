package org.sonarsource.plugins.report.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Project {

    private String organization;
    private String id;
    private String key;
    private String name;
    private String qualifier;
    private Date analysisDate;
    private List<String> tags;
    private String visibility;
    private Date leakPeriodDate;
    private String version;

}
