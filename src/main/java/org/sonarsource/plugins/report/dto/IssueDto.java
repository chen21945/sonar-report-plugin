package org.sonarsource.plugins.report.dto;

import lombok.Getter;
import lombok.Setter;
import org.sonarsource.plugins.report.model.*;

import java.util.List;

@Getter
@Setter
public class IssueDto {

    private Integer total;
    private Integer p;
    private Integer ps;
    private Paging paging;
    private List<Issue> issues;
    private List<Component> components;
    private List<Rule> rules;
    private List<Facet> facets;

}
