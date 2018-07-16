package org.sonarsource.plugins.report.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Analysis {

    private String key;

    private Date date;

    private List<Event> events;
}
