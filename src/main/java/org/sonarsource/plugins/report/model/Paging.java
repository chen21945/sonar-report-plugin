package org.sonarsource.plugins.report.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Paging {
    private Integer pageIndex;
    private Integer pageSize;
    private Integer total;
}
