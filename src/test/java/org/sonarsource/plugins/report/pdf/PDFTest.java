package org.sonarsource.plugins.report.pdf;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import org.junit.Test;
import org.sonarsource.plugins.report.support.PropertyUtils;
import org.sonarsource.plugins.report.support.exception.ReportException;
import org.sonarsource.plugins.report.support.pdf.PDFReporter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFTest {


    private static final String DEST = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\simple_table.pdf";


    @Test
    public void tableTest() throws FileNotFoundException {

        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(DEST));
        Document doc = new Document(pdfDoc);

        Table table = new Table(8);
        for (int i = 0; i < 16; i++) {
            table.addCell("hi");
        }
        doc.add(table);
        doc.close();
    }


    @Test
    public void getReportTest() throws ReportException, IOException {
        PDFReporter reporter = new PDFReporter("SVW.LRC");
        ByteArrayOutputStream baos = reporter.getReport();
        FileOutputStream fos = new FileOutputStream("C:/Users/yangchengang2.CNSVWSH00/desktop/report_pdf_test.pdf");
        baos.writeTo(fos);
        fos.flush();
        fos.close();
    }


    public static void main(String[] args) {
        System.out.println(PropertyUtils.class.getResourceAsStream("/report-texts.properties"));
    }

}
