package org.sonarsource.plugins.report.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.borders.RidgeBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import org.sonarsource.plugins.report.support.pdf.Style;

public class PDFUtils {

    public static String percent(String data) {
        return data == null ? null : data + "%";
    }


    public static Cell metricCell(String value, String text) {
        return metricCell(value, text, Border.NO_BORDER);
    }

    public static Cell metricCellBordered(String value, String text) {
        return metricCell(value, text, new GrooveBorder(0.1F));
    }

    private static Cell metricCell(String value, String text, Border border) {
        Paragraph ptext = Style.tableCellLarge().add(value);
        Paragraph pvalue = Style.tableCellMiddle().setFontColor(ColorConstants.DARK_GRAY).add(text);
        return new Cell().add(ptext).add(pvalue).setBorder(border);
    }


}
