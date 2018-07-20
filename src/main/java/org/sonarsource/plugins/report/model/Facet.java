package org.sonarsource.plugins.report.model;

import lombok.Getter;
import lombok.Setter;
import org.sonarsource.plugins.report.constant.SonarConstants;

import java.util.List;

@Getter
@Setter
public class Facet {

    private String property;

    private List<FacetValue> values;

    @Getter
    @Setter
    public class FacetValue {
        private SonarConstants.Severity val;
        private Integer count;
    }


}

