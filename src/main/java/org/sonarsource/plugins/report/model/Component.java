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

    /*issue列表用*/
    private String longName;

    private String path;

    private String uuid;

    private Integer count;

}
