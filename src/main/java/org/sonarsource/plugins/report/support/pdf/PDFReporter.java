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
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sonarsource.plugins.report.constant.MetricKeys;
import org.sonarsource.plugins.report.constant.ReportTexts;
import org.sonarsource.plugins.report.constant.SonarConstants;
import org.sonarsource.plugins.report.dto.IssueDto;
import org.sonarsource.plugins.report.model.*;
import org.sonarsource.plugins.report.service.ComponentService;
import org.sonarsource.plugins.report.support.PropertyUtils;
import org.sonarsource.plugins.report.support.exception.ReportException;
import org.sonarsource.plugins.report.util.DateUtils;
import org.sonarsource.plugins.report.util.PDFUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
        pdf.close();
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
        Paragraph paragraph = Style.chapterLevel1().add("1. ").add(getProject().getName())
                .add("    ")
                .add(qualityGate(project.getMeasureValue(MetricKeys.ALERT_STATUS)));
        document.add(paragraph);
        Paragraph text = Style.text().add(PropertyUtils.get(ReportTexts.MAIN_TEXT_MISC_OVERVIEW));
        document.add(text);
        //概述
        printOverView(document, "1.1 ");
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
        //问题分析
        printViolationsAnalysis(document, "1.2");
        //问题详情
        printViolationsDetail(document, "1.3");

    }

    private void printOverView(Document document, String number) {
        document.add(Style.chapterLevel2().add(number)
                .add(PropertyUtils.get(ReportTexts.GENERAL_REPORT_OVERVIEW)));
        printOverViewStatic(document);
        printOverViewDynamic(document);
    }

    private void printOverViewStatic(Document document) {
        //静态分析
        document.add(Style.chapterLevel2().add(PropertyUtils.get(ReportTexts.GENERAL_STATIC_ANALYSIS)));
        Table table = new Table(3)
                .setBorder(Border.NO_BORDER)
                .setMarginLeft(20)
                .setMarginRight(20)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout();
        Cell cell;
        Paragraph paragraph;

        //代码行数
        cell = PDFUtils.metricCell(this.project.getMeasureValue(MetricKeys.NCLOC), PropertyUtils.get(MetricKeys.NCLOC.getDesc()));
        //目录、类、方法
        paragraph = Style.tableCellMiddle()
                .setMarginTop(10)
                .add(PropertyUtils.get(MetricKeys.DICTIONARIES.getDesc()))
                .add(" ")
                .add(this.project.getMeasureValue(MetricKeys.DICTIONARIES));
        cell.add(paragraph);
        paragraph = Style.tableCellMiddle()
                .add(PropertyUtils.get(MetricKeys.CLASSES.getDesc()))
                .add(" ")
                .add(this.project.getMeasureValue(MetricKeys.CLASSES));
        cell.add(paragraph);

        paragraph = Style.tableCellMiddle().add(PropertyUtils.get(MetricKeys.FUNCTIONS.getDesc()))
                .add(" ")
                .add(this.project.getMeasureValue(MetricKeys.FUNCTIONS));
        cell.add(paragraph);

        table.addCell(cell);

        //注释
        cell = PDFUtils.metricCell(PDFUtils.percent(this.project.getMeasureValue(MetricKeys.COMMENT_LINES_DENSITY)),
                PropertyUtils.get(MetricKeys.COMMENT_LINES_DENSITY.getDesc()));

        paragraph = Style.tableCellMiddle()
                .setMarginTop(10)
                .add(PropertyUtils.get(MetricKeys.COMMENT_LINES.getDesc()))
                .add(" ")
                .add(this.project.getMeasureValue(MetricKeys.COMMENT_LINES));
        cell.add(paragraph);
        table.addCell(cell);

        //复杂度
        cell = PDFUtils.metricCell(this.project.getMeasureValue(MetricKeys.COMPLEXITY),
                PropertyUtils.get(MetricKeys.COMPLEXITY.getDesc()));

        paragraph = Style.tableCellMiddle()
                .setMarginTop(10)
                .add(PropertyUtils.get(MetricKeys.COGNITIVE_COMPLEXITY.getDesc()))
                .add(" ")
                .add(this.project.getMeasureValue(MetricKeys.COGNITIVE_COMPLEXITY));
        cell.add(paragraph);

        table.addCell(cell);
        document.add(table);
    }

    private void printOverViewDynamic(Document document) {
        document.add(Style.chapterLevel2().add(PropertyUtils.get(ReportTexts.GENERAL_DYNAMIC_ANALYSIS)));

        printQualityGate(document);
        printMetrics(document);
        printSeverity(document);
    }


    private void printQualityGate(Document document) {
        //qualityGate
        document.add(Style.text()
                .add(PropertyUtils.get(ReportTexts.GENERAL_ALERT_STATUS))
                .add("  ")
                .add(qualityGate(project.getMeasureValue(MetricKeys.ALERT_STATUS))));
        String detailStr = project.getMeasureValue(MetricKeys.QUALITY_GATE_DETAILS);
        //quality gate details
        QualityGateDetail qualityGateDetail = JSON.parseObject(detailStr, QualityGateDetail.class);
        if (SonarConstants.QUALITY_GATE_OK.equals(qualityGateDetail.getLevel())) {
            return;
        }
        List<QualityGateDetail.QualityGateCondition> errors = qualityGateDetail.getConditions().stream().filter(
                condition -> SonarConstants.QUALITY_GATE_ERROR.equals(condition.getLevel())
        ).collect(Collectors.toList());
        if (errors.size() == 0) {
            return;
        }
        Table table = new Table(errors.size())
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginLeft(20);
        errors.forEach(condition -> {
            String value = condition.getActual();
            MetricKeys metricKeys = MetricKeys.get(condition.getMetric());
            String metricName = metricKeys != null ? PropertyUtils.get(metricKeys.getDesc()) : condition.getMetric();
            Cell cell = PDFUtils.metricCellBordered(value, metricName);
            Paragraph paragraph = Style.smallText().add("is greater than ").add(condition.getError());
            cell.add(paragraph);
            table.addCell(cell);
        });
        document.add(table);

    }

    private void printMetrics(Document document) {
        document.add(Style.text()
                .add(PropertyUtils.get(ReportTexts.GENERAL_CORE_METRICS)));
        Table table = new Table(3)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginLeft(20)
                .setFixedLayout();
        List<MetricKeys> metricKeys = Arrays.asList(
                MetricKeys.BUGS, MetricKeys.SECURITY,
                MetricKeys.MAINTAINABILITY, MetricKeys.COVERAGE,
                MetricKeys.DUPLICATE_LINE_DENSITY, MetricKeys.DUPLICATED_BLOCKS
        );
        metricKeys.forEach(
                metricKey -> {
                    Measure measure = project.getMeasure(metricKey);
                    if (measure == null) {
                        return;
                    }
                    Cell cell = PDFUtils.metricCellBordered(measure.getValue(), PropertyUtils.get(metricKey.getDesc()));
                    table.addCell(cell);
                }
        );
        document.add(table);
    }

    private void printSeverity(Document document) {
        document.add(Style.text()
                .add(PropertyUtils.get(ReportTexts.GENERAL_SEVERITY)));
        Table table = new Table(5)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(100))
                .setMarginLeft(20)
                .setFixedLayout();
        List<MetricKeys> metricKeys = Arrays.asList(
                MetricKeys.BLOCKER_VIOLATIONS, MetricKeys.CRITICAL_VIOLATIONS,
                MetricKeys.MAJOR_VIOLATIONS, MetricKeys.MINOR_VIOLATIONS,
                MetricKeys.INFO_VIOLATIONS
        );
        metricKeys.forEach(
                metricKey -> {
                    Measure measure = project.getMeasure(metricKey);
                    if (measure == null) {
                        return;
                    }
                    Cell cell = PDFUtils.metricCellBordered(measure.getValue(), PropertyUtils.get(metricKey.getDesc()));
                    table.addCell(cell);
                }
        );
        document.add(table);
    }

    private void printViolationsAnalysis(Document document, String number) {
        document.add(Style.chapterLevel2().add(number)
                .add(PropertyUtils.get(ReportTexts.GENERAL_VIOLATIONS_ANALYSIS)));
    }


    private void printViolationsDetail(Document document, String number) {
        document.add(Style.chapterLevel2().add(number)
                .add(PropertyUtils.get(ReportTexts.GENERAL_VIOLATIONS_DETAILS)));
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
     * @throws ReportException
     */
    private Project getProject() throws ReportException {
        if (this.project != null && this.project.getId() != null) {
            return project;
        }
        this.project = ComponentService.getInstance().getProjcet(this.projectKey);
        if (this.project == null) {
            throw new ReportException("can not get project, key={" + this.projectKey + "}");
        }
        log.info("transfer to project success, project={}" + JSON.toJSONString(this.project));
        getAnalysisData();
        return this.project;
    }

    public Project project() {
        return this.project;
    }

    private void getAnalysisData() {
        //分析指标
        List<Measure> measures = ComponentService.getInstance().getMeasures(this.projectKey, metricKeys());
        Map<String, Measure> measureMap = measures.stream().collect(Collectors.toMap(Measure::getMetric, measure -> measure));
        this.project.setMeasureMap(measureMap);
        //分析结果
        List<Analysis> analyses = ComponentService.getInstance().getAnalysis(this.projectKey, 1, 1);
        if (analyses.size() > 0) {
            this.project.setAnalysis(analyses.get(0));
        }
        //issues
        IssueDto dto = ComponentService.getInstance().getIssues(this.projectKey,
                Arrays.asList("severities", "types", "rules", "fileUuids", "languages"),
                100, 1);
        if (dto != null) {
            List<Facet> facets = dto.getFacets();
            if (facets != null && facets.size() > 0) {
                facets.forEach(facet -> {
                    switch (facet.getProperty()) {
                        case "severities":
                            project.setSeverities(facet.getValues());
                            break;
                        case "types":
                            project.setIssueTypes(facet.getValues());
                            break;
                        case "languages":
                            project.setLanguages(facet.getValues());
                            break;
                        case "rules":
                            if (dto.getRules() == null || dto.getRules().size() == 0) {
                                break;
                            }
                            Map<String, Rule> mapRules = dto.getRules().stream().collect(Collectors.toMap(Rule::getKey, rule -> rule));
                            List<Rule> rules = facet.getValues().stream().map(
                                    value -> {
                                        Rule rule = mapRules.get(value.getKey());
                                        rule.setCount(value.getValue());
                                        return rule;
                                    }
                            ).collect(Collectors.toList());
                            project.setRules(rules);
                            break;
                        case "fileUuids":
                            List<Component> components = dto.getComponents();
                            if (components == null || components.size() == 0) {
                                break;
                            }
                            Map<String, Component> componentMap =
                                    components.stream().collect(Collectors.toMap(Component::getUuid, component -> component));
                            List<Component> components1 = facet.getValues().stream().map(
                                    value -> {
                                        Component component = componentMap.get(value.getKey());
                                        component.setCount(value.getValue());
                                        return component;
                                    }
                            ).collect(Collectors.toList());
                            project.setComponents(components1);
                            break;
                        default:
                    }
                });
            }
        }
    }

    private List<String> metricKeys() {
        return Arrays.stream(MetricKeys.values()).map(MetricKeys::getKey).collect(Collectors.toList());
    }


    private Text qualityGate(String result) {
        Text qualityGate = new Text("")
                .setFontColor(ColorConstants.WHITE)
                .setBackgroundColor(ColorConstants.RED);

        if (StringUtils.isEmpty(result)) {
            return qualityGate;
        }
        switch (result) {
            case SonarConstants.QUALITY_GATE_ERROR:
                qualityGate.setText(" Failed  ");
                break;
            case SonarConstants.QUALITY_GATE_OK:
                qualityGate.setText(" Passed  ");
                break;
            default:
                qualityGate = null;
        }
        return qualityGate;
    }

}
