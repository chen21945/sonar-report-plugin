package org.sonarsource.plugins.report.model;

import lombok.Data;
import org.sonarsource.plugins.report.constant.MetricKeys;

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    private Map<String, Measure> measureMap;
    private Analysis analysis;
    private List<Component> components;
    private List<Rule> rules;
    private List<Facet.FacetValue> severities;
    private List<Facet.FacetValue> issueTypes;
    private List<Facet.FacetValue> languages;

    public Measure getMeasure(MetricKeys metricKey) {
        if (metricKey == null) {
            return null;
        }
        if (measureMap == null || measureMap.size() == 0) {
            return null;
        }
        return measureMap.get(metricKey.getKey());
    }

    public String getMeasureValue(MetricKeys metricKey) {
        Measure measure = getMeasure(metricKey);
        return measure == null ? null : measure.getValue();
    }

}
