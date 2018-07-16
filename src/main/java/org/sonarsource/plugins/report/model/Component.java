package org.sonarsource.plugins.report.model;

import lombok.Data;

import java.util.List;

@Data
public class Component {

    private String id;

    private String key;

    private String name;

    private String qualifier;

    private List<Measure> measures;

}
