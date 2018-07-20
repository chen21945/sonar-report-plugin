package org.sonarsource.plugins.report.pdf;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;

@Slf4j
public class FontTest {

    public void initFont() {
        PdfFont font = null;
        try {
            font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", true);
        } catch (IOException e) {
            log.error("error load microsoft yahei font.", e);
        }
    }


    @Test
    public void fontTest() throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        List list = new List();
        list.add("项目任务书");
        list.add("SVW LRC");
        list.add("2018-09-30");

        PdfFont font1 = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
        Paragraph paragraph = new Paragraph().setFont(font1)
                .add("项目任务书")
                .add("SVW LRC")
                .add("2018-09-30");
        doc.add(paragraph);

        PdfFont font2 = PdfFontFactory.createFont("MHei-Medium", "UniGB-UCS2-H", false);
        Paragraph paragraph2 = new Paragraph().setFont(font2).add("项目任务书")
                .add("SVW LRC")
                .add("2018-09-30");
        doc.add(paragraph2);

        PdfFont font3 = PdfFontFactory.createFont("MSungStd-Light", "UniGB-UCS2-H", false);
        Paragraph paragraph3 = new Paragraph().setFont(font3).add("项目任务书")
                .add("SVW LRC")
                .add("2018-09-30");
        doc.add(paragraph3);

        doc.close();
    }

    private static final String DEST = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\font_test.pdf";


}
