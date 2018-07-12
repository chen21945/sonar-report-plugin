package org.sonarsource.plugins.report.pdf;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.property.TabAlignment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class TabStopsTest {

    public static final String DEST = "C:/Users/yangchengang2.CNSVWSH00/desktop/tabstops_test.pdf";

    public static void main(String[] args) throws FileNotFoundException {

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        PageSize pageSize = pdfDoc.getDefaultPageSize();
        Document document = new Document(pdfDoc);

        float w = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();
        List<TabStop> tabstops = new ArrayList();
        tabstops.add(new TabStop(w / 2, TabAlignment.CENTER));
        tabstops.add(new TabStop(w, TabAlignment.LEFT));

        // 段落
        Paragraph p = new Paragraph();
        p.addTabStops(tabstops);
        p.add(new Tab()).add("Text in the middle").add(new Tab());
        p.add(new Tab()).add("How To Create An PDF File?").add(new Tab());

        document.add(p);
        document.close();
    }
}
