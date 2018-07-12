package org.sonarsource.plugins.report.pdf;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfOutline;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.kernel.pdf.navigation.PdfExplicitDestination;
import com.itextpdf.layout.ColumnDocumentRenderer;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.renderer.DrawContext;
import com.itextpdf.layout.renderer.TextRenderer;

import java.util.ArrayList;

public class CreateTOCinColumnTest {

    public static final String DEST = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\create_toc_in_column.pdf";

    protected java.util.List<TOCEntry> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        new CreateTOCinColumnTest().manipulatePdf(DEST);
    }

    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);
        Rectangle[] columns = {
                new Rectangle(36, 36, 173, 770),
                new Rectangle(213, 36, 173, 770),
                new Rectangle(389, 36, 173, 770)
        };
        doc.setRenderer(new ColumnDocumentRenderer(doc, columns));
        PdfOutline root = pdfDoc.getOutlines(false);
        int start;
        int end;
        for (int i = 0; i <= 20; ) {
            start = (i * 10) + 1;
            i++;
            end = i * 10;
            String title = String.format("Numbers from %s to %s", start, end);
            Text c = new Text(title);
            TOCTextRenderer renderer = new TOCTextRenderer(c);
            renderer.setRoot(root);
            c.setNextRenderer(renderer);
            doc.add(new Paragraph(c));
            doc.add(createTable(start, end));
        }
        doc.add(new AreaBreak());
        for (TOCEntry entry : list) {
            Link c = new Link(entry.title, entry.dest);
            doc.add(new Paragraph(c));
        }
        doc.close();
    }

    protected Table createTable(int start, int end) {
        Table table = new Table(2);
        for (int i = start; i <= end; i++) {
            table.addCell(new Cell().add(new Paragraph(String.valueOf(i))));
            table.addCell(new Cell().add(new Paragraph("Test")));
        }
        return table;
    }


    protected class TOCEntry {
        protected String title;
        protected PdfDestination dest;

        public TOCEntry(String title, PdfDestination dest) {
            this.dest = dest;
            this.title = title;
        }
    }


    protected class TOCTextRenderer extends TextRenderer {
        protected PdfOutline root;

        public TOCTextRenderer(Text modelElement) {
            super(modelElement);
        }

        public void setRoot(PdfOutline root) {
            this.root = root;
        }

        @Override
        public void draw(DrawContext drawContext) {
            super.draw(drawContext);
            Rectangle rect = getOccupiedAreaBBox();
            PdfDestination dest = PdfExplicitDestination.createXYZ(drawContext.getDocument().getLastPage(),
                    rect.getLeft(), rect.getTop(), 0);
            list.add(new TOCEntry(((Text) modelElement).getText(), dest));

            PdfOutline curOutline = root.addOutline(((Text) modelElement).getText());
            curOutline.addDestination(dest);
        }
    }

}
