package org.sonarsource.plugins.report.util;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.sonarsource.plugins.report.constant.ReportTexts;
import org.sonarsource.plugins.report.constant.SonarConstants;
import org.sonarsource.plugins.report.model.Facet;
import org.sonarsource.plugins.report.support.pdf.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PDFUtils {

    public static String percent(String data) {
        return data == null ? null : data + "%";
    }


    public static String getSeverityText(SonarConstants.Severity severity) {
        switch (severity.getKey()) {
            case "INFO":
                return ReportTexts.GENERAL_INFO_VIOLATIONS;
            case "MINOR":
                return ReportTexts.GENERAL_MINOR_VIOLATIONS;
            case "MAJOR":
                return ReportTexts.GENERAL_MAJOR_VIOLATIONS;
            case "CRITICAL":
                return ReportTexts.GENERAL_CRITICAL_VIOLATIONS;
            case "BLOCKER":
                return ReportTexts.GENERAL_BLOCKER_VIOLATIONS;
            default:
                return "";
        }
    }

    public static SonarConstants.Severity getSeverity(String metricKey) {
        switch (metricKey) {
            case ("blocker_violations"):
                return SonarConstants.Severity.BLOCKER;
            case ("critical_violations"):
                return SonarConstants.Severity.CRITICAL;
            case ("major_violations"):
                return SonarConstants.Severity.MAJOR;
            case ("minor_violations"):
                return SonarConstants.Severity.MINOR;
            case ("info_violations"):
                return SonarConstants.Severity.INFO;
            default:
                return null;
        }
    }

    public static Paragraph issueFileName(String file) {
        int pathIndex = file.lastIndexOf(":");
        int nameIndex = file.lastIndexOf("/");
        if (nameIndex < 0) {
            nameIndex = pathIndex;
        }
        String path = file.substring(pathIndex + 1, nameIndex + 1);
        String name = file.substring(nameIndex + 1);
        return Style.tableText()
                .setBackgroundColor(Style.MyColor.COLOR_SLATE_GREY)
                .add(path)
                .add(new Text(name).setBold());
    }

    public static String lineStr(Integer line) {
        return line == null ? "" : "L" + line;
    }

    public static List<Facet.FacetValue> sortSeverity(List<Facet.FacetValue> severities) {
        List<SonarConstants.Severity> sorted = Arrays.asList(
                SonarConstants.Severity.BLOCKER, SonarConstants.Severity.CRITICAL, SonarConstants.Severity.MAJOR,
                SonarConstants.Severity.MINOR, SonarConstants.Severity.INFO);
        List<Facet.FacetValue> sortedSeverity = new ArrayList<>();
        for (SonarConstants.Severity severity : sorted) {
            for (Facet.FacetValue value : severities) {
                if (severity.equals(value.getVal())) {
                    sortedSeverity.add(value);
                    break;
                }
            }
        }
        return sortedSeverity;
    }
}
