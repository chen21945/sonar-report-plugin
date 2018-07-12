package org.sonarsource.plugins.report.support.pdf;

import com.alibaba.fastjson.JSON;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import org.sonarsource.plugins.report.model.Project;
import org.sonarsource.plugins.report.service.ComponentService;
import org.sonarsource.plugins.report.support.exception.ReportException;
import org.sonarsource.plugins.report.util.DateUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class PDFReporter {

    private String projectKey;
    private Project project;

    public PDFReporter(String projectKey) {
        this.projectKey = projectKey;
    }

    public ByteArrayOutputStream getReport() throws IOException, ReportException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(outputStream));
        //大纲模式
        pdf.getCatalog().setPageMode(PdfName.UseOutlines);
        //新增document
        Document document = new Document(pdf, PageSize.A4);

        //header footer
        HeaderFooter headerFooter = new HeaderFooter(document)
                .setSonarLogo(getLogo())
                .setSvwLogo(getSvwLogo())
                .setProjectNm(this.getProject().getName());
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, headerFooter);

        //文档默认属性
        PdfFont font = Style.microsoftYaHei();
        document.setTextAlignment(TextAlignment.JUSTIFIED)
                .setFont(font)
                .setFontSize(12)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMargins(headerFooter.getHeight() + 20, 36, 36, 36);
        log.info("printing front page");


        printFrontPage(document);

        printBodyPage(document);

        for (int i = 0; i <= 200; i++) {
            document.add(new Paragraph("line-" + i + ",Hello World!"));
            if (i % 7 == 0) {
                document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            }
        }
        document.close();
        return outputStream;
    }

    /**
     * 首页
     *
     * @param document
     * @throws IOException
     * @throws ReportException
     */
    private void printFrontPage(Document document) throws IOException, ReportException {
        //首页logo
        Image image = new Image(ImageDataFactory.create(getLogo()))
                .setWidth(document.getPdfDocument().getDefaultPageSize().getWidth() * 2 / 3)
                .setFixedPosition(114, 542);
        document.add(image);

        PageSize pageSize = document.getPdfDocument().getDefaultPageSize();
        //首页显示的项目信息
        java.util.List<String> list = Arrays.asList(
                project.getName(),
                "version-" + project.getVersion(),
                DateUtils.format(getProject().getAnalysisDate(), DateUtils.DATE_FORMAT)
        );

        AtomicInteger i = new AtomicInteger();
        list.forEach(str -> {
            Paragraph paragraph = new Paragraph()
                    .addTabStops(new TabStop((pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin()) / 2, TabAlignment.CENTER))
                    .setFontSize(24)
                    .setFixedPosition(document.getLeftMargin(), 300 - i.getAndIncrement() * 40, pageSize.getWidth())
                    .add(new Tab())
                    .add(str);
            document.add(paragraph);
        });
        //添加换页
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
    }

    private void printBodyPage(Document document) throws IOException, ReportException {
        Project project = getProject();
        if (project == null) {
            return;
        }
        PageSize pageSize = document.getPdfDocument().getDefaultPageSize();
        Paragraph paragraph = Style.chapterLevel1()
                .add("1.第一张");
        document.add(paragraph);
        Paragraph paragraph1 = Style.text()
                .add("为了魏汝稳娥儿我客人问饿热望日看接客人来看额我客人额了看我")
                .add("sfkwjlkfe jlkewj欸惹我金融i列举额我看了人家看了我就立刻进入陆克文就");
        document.add(paragraph1);


    }


    private URL getLogo() {
        return this.getClass().getResource("/static/img/sonarqube.png");
    }

    private URL getSvwLogo() {
        return this.getClass().getResource("/static/img/csvw.jpg");
    }

    /**
     * 查询项目信息
     *
     * @return Project
     * @throws IOException
     */
    private Project getProject() throws IOException, ReportException {
        if (this.project != null && this.project.getId() != null) {
            return project;
        }
        this.project = new ComponentService().getProjcet(this.projectKey);
        if (this.project == null) {
            throw new ReportException("can not get project, key={" + this.projectKey + "}");
        }
        log.info("transfer to project success, project={}" + JSON.toJSONString(this.project));
        return this.project;
    }

}
