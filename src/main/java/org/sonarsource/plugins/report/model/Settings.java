package org.sonarsource.plugins.report.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Settings {

    private String key;
    private String value;
    private String parentValue;

    private List<String> values;
    private List<String> parentValues;


}
