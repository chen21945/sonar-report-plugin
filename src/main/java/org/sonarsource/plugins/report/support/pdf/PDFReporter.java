package org.sonarsource.plugins.report.support.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.ce.posttask.Project;
import org.sonarsource.plugins.report.support.*;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;

@Slf4j
public class PDFReporter {

    private String projectKey;
    private Project project;

    public PDFReporter(String projectKey) {
        this.projectKey = projectKey;
    }

    public ByteArrayOutputStream getReport() throws DocumentException, IOException, ReportException {
        // Creation of documents
        Document mainDocument = new Document(PageSize.A4, 50, 50, 110, 50);
        Toc tocDocument = new Toc();
        Document frontPageDocument = new Document(PageSize.A4, 50, 50, 110, 50);
        ByteArrayOutputStream mainDocumentBaos = new ByteArrayOutputStream();
        ByteArrayOutputStream frontPageDocumentBaos = new ByteArrayOutputStream();
        PdfWriter mainDocumentWriter = PdfWriter.getInstance(mainDocument,
                mainDocumentBaos);
        PdfWriter frontPageDocumentWriter = PdfWriter.getInstance(
                frontPageDocument, frontPageDocumentBaos);

        // Events for TOC, header and pages numbers
        Events events = new Events(tocDocument, new Header(this.getLogo(),
                this.getProject()));
//        mainDocumentWriter.setPageEvent(events);

        mainDocument.open();
        tocDocument.getTocDocument().open();
        frontPageDocument.open();

        log.info("Generating PDF report...");
        printFrontPage(frontPageDocument, frontPageDocumentWriter);
        printTocTitle(tocDocument);
        printPdfBody(mainDocument);
        mainDocument.close();
        tocDocument.getTocDocument().close();
        frontPageDocument.close();

        // Get Readers
        PdfReader mainDocumentReader = new PdfReader(mainDocumentBaos.toByteArray());
        PdfReader tocDocumentReader = new PdfReader(tocDocument
                .getTocOutputStream().toByteArray());
        PdfReader frontPageDocumentReader = new PdfReader(
                frontPageDocumentBaos.toByteArray());

        // New document
        Document documentWithToc = new Document(
                tocDocumentReader.getPageSizeWithRotation(1));
        ByteArrayOutputStream finalBaos = new ByteArrayOutputStream();
        PdfCopy copy = new PdfCopy(documentWithToc, finalBaos);

        documentWithToc.open();
        copy.addPage(copy.getImportedPage(frontPageDocumentReader, 1));
        for (int i = 1; i <= tocDocumentReader.getNumberOfPages(); i++) {
            copy.addPage(copy.getImportedPage(tocDocumentReader, i));
        }
        for (int i = 1; i <= mainDocumentReader.getNumberOfPages(); i++) {
            copy.addPage(copy.getImportedPage(mainDocumentReader, i));
        }
        documentWithToc.close();
        return finalBaos;
    }


    private URL getLogo() {
        return this.getClass().getResource("/static/img/logo.png");
    }

    private Project getProject() {
        Project project = null;
        return this.project == null ? project : this.project;
    }

    protected void printFrontPage(Document frontPageDocument, PdfWriter frontPageWriter)
            throws ReportException {
        try {
            URL largeLogo = getLogo();
            Image logoImage = Image.getInstance(largeLogo);
            logoImage.scaleAbsolute(360, 200);
            Rectangle pageSize = frontPageDocument.getPageSize();
            logoImage.setAbsolutePosition(Style.FRONTPAGE_LOGO_POSITION_X,
                    Style.FRONTPAGE_LOGO_POSITION_Y);
            frontPageDocument.add(logoImage);

            PdfPTable title = new PdfPTable(1);
            title.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            title.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            String projectRow = this.getProject().getName();
            String versionRow = "this is version row";
// this.getProject().getMeasures().getVersion();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            String dateRow = "this is the date row";
//                    df.format(this.getProject().getMeasures().getDate());
            String descriptionRow = "this is the description row";
//                    super.getProject().getDescription();
            String profileRow = "this is the Quality profile row";
//            super.getProject().getMeasure(MetricKeys.PROFILE).getDataValue()

            title.addCell(new Phrase(projectRow, Style.FRONTPAGE_FONT_1));
            title.addCell(new Phrase(versionRow, Style.FRONTPAGE_FONT_1));
            title.addCell(new Phrase(descriptionRow, Style.FRONTPAGE_FONT_2));
            title.addCell(new Phrase(profileRow, Style.FRONTPAGE_FONT_3));
            title.addCell(new Phrase(dateRow, Style.FRONTPAGE_FONT_3));
            title.setTotalWidth(pageSize.getWidth() - frontPageDocument.leftMargin() - frontPageDocument.rightMargin());
            title.writeSelectedRows(
                    0,
                    -1,
                    frontPageDocument.leftMargin(),
                    Style.FRONTPAGE_LOGO_POSITION_Y - 150,
                    frontPageWriter.getDirectContent());
        } catch (IOException | DocumentException e) {
            log.error("Can not generate front pages", e);
        }
    }

    protected void printTocTitle(Toc tocDocument)
            throws DocumentException, IOException {
        Paragraph tocTitle = new Paragraph(PropertyUtils.get("main.table.of.contents"), Style.TOC_TITLE_FONT);
        tocTitle.setAlignment(Element.ALIGN_CENTER);
        tocDocument.getTocDocument().add(tocTitle);
        tocDocument.getTocDocument().add(Chunk.NEWLINE);
    }

    protected void printPdfBody(Document document)
            throws DocumentException, IOException, ReportException {
        Project project = getProject();
        // Chapter 1: Report Overview (Parent project)
        ChapterAutoNumber chapter1 = new ChapterAutoNumber(new Paragraph(
                project.getName(), Style.CHAPTER_FONT));
        chapter1.add(new Paragraph(PropertyUtils.get("main.text.misc.overview"),
                Style.NORMAL_FONT));
//        Section section11 = chapter1.addSection(new Paragraph(
//                PropertyUtils.get("general.report_overview"), Style.TITLE_FONT));
//        printDashboard(project, section11);
//        Section section12 = chapter1.addSection(new Paragraph(
//                PropertyUtils.get("general.violations_analysis"), Style.TITLE_FONT));
//        printMostViolatedRules(project, section12);
//        printMostViolatedFiles(project, section12);
//        printMostComplexFiles(project, section12);
//        printMostDuplicatedFiles(project, section12);
        document.add(chapter1);

//        Iterator<Project> it = project.getSubprojects().iterator();
//        while (it.hasNext()) {
//            Project subproject = it.next();
//            ChapterAutoNumber chapterN = new ChapterAutoNumber(new Paragraph(
//                    subproject.getName(), Style.CHAPTER_FONT));
//
//            Section sectionN1 = chapterN.addSection(new Paragraph(
//                    getTextProperty("general.report_overview"), Style.TITLE_FONT));
//            printDashboard(subproject, sectionN1);
//
//            Section sectionN2 = chapterN.addSection(new Paragraph(
//                    getTextProperty("general.violations_analysis"), Style.TITLE_FONT));
//            printMostViolatedRules(subproject, sectionN2);
//            printMostViolatedFiles(subproject, sectionN2);
//            printMostComplexFiles(subproject, sectionN2);
//            printMostDuplicatedFiles(subproject, sectionN2);
//            document.add(chapterN);
//        }
    }

    private void printDashboard(final Project project, final Section section)
            throws DocumentException {

    }
}
