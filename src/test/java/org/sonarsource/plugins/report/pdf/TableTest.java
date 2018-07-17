package org.sonarsource.plugins.report.pdf;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.canvas.draw.ILineDrawer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.LineSeparatorRenderer;
import org.junit.Test;
import org.sonarsource.plugins.report.support.pdf.Style;

import java.io.FileNotFoundException;

public class TableTest {

    public static final String DEST = "C:/Users/yangchengang2.CNSVWSH00/desktop//indent_table.pdf";


    @Test
    public void manipulatePdf() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

//        PdfCanvas cb = new PdfCanvas(pdfDoc.addNewPage());
//        cb.moveTo(36, 842);
//        cb.lineTo(36, 0);
//        cb.stroke();
        Table table = new Table(8);
        table.setHorizontalAlignment(HorizontalAlignment.LEFT);
        table.setWidth(150);
        for (int aw = 0; aw < 16; aw++) {
            table.addCell(new Cell().add(new Paragraph("hi")));
        }
        table.setMarginLeft(25);
        doc.add(table);

        doc.close();
    }


    @Test
    public void manipulatePdf1() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        Table table = new Table(1);
        Cell cell;
        cell = new Cell().add(new Paragraph("TO:\n\n   name"));
        table.addCell(cell);
        cell = new Cell().add(new Paragraph("TO:\n\n\u00a0\u00a0\u00a0name"));
        table.addCell(cell);
        cell = new Cell();
        cell.add(new Paragraph("TO:"));
        Paragraph p = new Paragraph("name");
        p.setMarginLeft(10);
        cell.add(p);
        table.addCell(cell);
        cell = new Cell();
        cell.add(new Paragraph("TO:"));
        p = new Paragraph("name");
        p.setTextAlignment(TextAlignment.RIGHT);
        cell.add(p);
        table.addCell(cell);
        doc.add(table);
        doc.close();

    }


    @Test
    public void manipulatePdf2() throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        // Here in itext7 there is only one way of adding paragraph to table. See itext5 example.
        Table table = new Table(1);
        Paragraph right = new Paragraph("This is right, because we create a paragraph with an indentation to the left and as we are adding the paragraph in composite mode, all the properties of the paragraph are preserved.");
        right.setMarginLeft(20);
        Cell rightCell = new Cell().add(right);
        table.addCell(rightCell);
        doc.add(table);
        doc.close();
    }


    @Test
    public void tableCellTest() throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        Table table = new Table(2);

        table.addHeaderCell("this is header cell");
        Paragraph p = Style.tableCellLarge().add("行数");
        p.setUnderline(ColorConstants.GRAY, 0.5F, 0, 0, -0.4F, PdfCanvasConstants.LineCapStyle.BUTT);

        Paragraph p1 = Style.tableCellMiddle().add("12312");

        Cell cell = new Cell().add(p).add(p1);
        table.addCell(cell);
        table.addCell("12312");
        doc.add(table);
        doc.close();
    }


}
