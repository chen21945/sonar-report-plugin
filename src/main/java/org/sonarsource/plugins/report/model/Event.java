package org.sonarsource.plugins.report.model;

import lombok.Data;

@Data
public class Event {

    private String key;

    private String name;

    private String description;

    private String category;

}
