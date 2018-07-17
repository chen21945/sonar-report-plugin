package org.sonarsource.plugins.report.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Facet {

    private String property;

    private List<FacetValue> values;

    @Getter
    @Setter
    public class FacetValue {
        private String key;
        private Integer value;
    }


}

