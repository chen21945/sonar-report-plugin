package org.sonarsource.plugins.report.pdf;


import com.itextpdf.io.IOException;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;

public class ColumnTextAscender{
    public static final String DEST = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\column_text_ascender.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextAscender().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException, FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        Rectangle[] areas = new Rectangle[] {new Rectangle(50, 750, 200, 50), new Rectangle(300, 750, 200, 50)};
        // for canvas usage one should create a page
        pdfDoc.addNewPage();
        for (Rectangle rect : areas) {
            new PdfCanvas(pdfDoc.getFirstPage()).setLineWidth(0.5f).rectangle(rect).stroke();
        }
        doc.setRenderer(new ColumnDocumentRenderer(doc, areas));
        addColumn(doc, false);
        addColumn(doc, true);
        doc.close();
    }

    public void addColumn(Document doc, boolean useAscender) {
        Paragraph p = new Paragraph("This text is added at the top of the column.");
        // No setUseAscender(boolean). We can change leading instead. This is a bit different, but
        // for now we do not see the need to implement this method in iText7
        if (useAscender) {
            p.setMargin(0);
            p.setMultipliedLeading(1);
        }

        doc.add(p);
    }
}