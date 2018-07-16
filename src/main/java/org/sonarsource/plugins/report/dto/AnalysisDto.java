package org.sonarsource.plugins.report.dto;

import lombok.Getter;
import lombok.Setter;
import org.sonarsource.plugins.report.model.Analysis;
import org.sonarsource.plugins.report.model.Paging;

import java.util.List;

@Getter
@Setter
public class AnalysisDto {

    private Paging paging;

    private List<Analysis> analyses;


}
