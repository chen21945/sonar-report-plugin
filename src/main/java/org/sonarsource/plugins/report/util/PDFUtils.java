package org.sonarsource.plugins.report.util;

import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import org.sonarsource.plugins.report.constant.ReportTexts;
import org.sonarsource.plugins.report.constant.SonarConstants;
import org.sonarsource.plugins.report.support.pdf.Style;

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


    public static Paragraph issueFileName(String file) {
        int nameIndex = file.lastIndexOf("/") + 1;
        String path = file.substring(file.indexOf(":") + 1, nameIndex);
        String name = file.substring(nameIndex);
        return Style.tableText()
                .setBackgroundColor(Style.MyColor.COLOR_SLATE_GREY)
                .add(path)
                .add(new Text(name).setBold());
    }
}
