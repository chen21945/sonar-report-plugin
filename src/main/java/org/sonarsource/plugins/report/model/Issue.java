package org.sonarsource.plugins.report.model;

import lombok.Getter;
import lombok.Setter;
import org.sonarsource.plugins.report.constant.SonarConstants;

import java.util.Date;

@Getter
@Setter
public class Issue {

    private String key;
    private String rule;
    private SonarConstants.Severity severity;
    private String component;
    private String project;
    private Integer line;
    private String hash;
    private String message;
    private Date creationDate;
    private Date updateDate;
    private SonarConstants.IssueType type;
    private String organization;
}
