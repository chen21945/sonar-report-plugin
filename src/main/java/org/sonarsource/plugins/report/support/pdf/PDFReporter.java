package org.sonarsource.plugins.report.support.pdf;

import com.alibaba.fastjson.JSON;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
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
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
public class PDFReporter {

    private String projectKey;
    private Project project;
    private List<String> issueTypes;

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
        document.setTextAlignment(TextAlignment.JUSTIFIED)
                .setFont(PdfFontFactory.createFont(Style.microsoftYaHei(), PdfEncodings.IDENTITY_H, true))
                .setFontSize(12)
                .setFontColor(ColorConstants.DARK_GRAY)
                .setMargins(headerFooter.getHeight() + Style.MARGIN_LEFT, 36, 36, 36);

        printFrontPage(document);

        printBodyPage(document);

        log.info("print success");
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
    private void printFrontPage(Document document) throws ReportException {
        log.info("printing front page");
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
        log.info("printing body page");
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
        //问题详情
        printViolationsDetail(document, "1.2");

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
                .setMarginLeft(Style.MARGIN_LEFT)
                .setMarginRight(Style.MARGIN_LEFT)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setWidth(UnitValue.createPercentValue(100))
                .setFixedLayout();
        Cell cell;
        Paragraph paragraph;

        //代码行数
        cell = Style.metricCell(this.project.getMeasureValue(MetricKeys.NCLOC), PropertyUtils.get(MetricKeys.NCLOC.getDesc()));
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
        cell = Style.metricCell(PDFUtils.percent(this.project.getMeasureValue(MetricKeys.COMMENT_LINES_DENSITY)),
                PropertyUtils.get(MetricKeys.COMMENT_LINES_DENSITY.getDesc()));

        paragraph = Style.tableCellMiddle()
                .setMarginTop(10)
                .add(PropertyUtils.get(MetricKeys.COMMENT_LINES.getDesc()))
                .add(" ")
                .add(this.project.getMeasureValue(MetricKeys.COMMENT_LINES));
        cell.add(paragraph);
        table.addCell(cell);

        //复杂度
        cell = Style.metricCell(this.project.getMeasureValue(MetricKeys.COMPLEXITY),
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
        PageSize pageSize = document.getPdfDocument().getDefaultPageSize();
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
        int columns = errors.size() > 4 ? 4 : errors.size();
        float cellWidth = (pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin() - Style.MARGIN_LEFT) / 4;
        Table table = new Table(columns)
                .setTextAlignment(TextAlignment.CENTER)
                .setHorizontalAlignment(HorizontalAlignment.LEFT)
                .setMarginLeft(Style.MARGIN_LEFT);
        errors.forEach(condition -> {
            String value = condition.getActual();
            MetricKeys metricKeys = MetricKeys.get(condition.getMetric());
            String metricName = metricKeys != null ? PropertyUtils.get(metricKeys.getDesc()) : condition.getMetric();
            Cell cell = Style.metricCellBordered(value, metricName).setWidth(cellWidth);
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
                .setMarginLeft(Style.MARGIN_LEFT)
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
                    Cell cell = Style.metricCellBordered(measure.getValue(), PropertyUtils.get(metricKey.getDesc()));
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
                .setMarginLeft(Style.MARGIN_LEFT)
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
                    Cell cell = Style.metricCellBorderedWithImg(
                            measure.getValue(),
                            PropertyUtils.get(metricKey.getDesc()),
                            getSeverityImg(PDFUtils.getSeverity(metricKey.getKey())));
                    table.addCell(cell);
                }
        );
        document.add(table);
    }


    private void printViolationsDetail(Document document, String number) {
        document.add(Style.chapterLevel2().add(number)
                .add(PropertyUtils.get(ReportTexts.GENERAL_VIOLATIONS_DETAILS)));
        List<SonarConstants.IssueType> types = getTypes();

        for (SonarConstants.IssueType type : types) {
            List<Issue> issues = project.getIssueMap().get(type);
            if (issues == null || issues.size() == 0) {
                continue;
            }

            String typeStr;
            switch (type.getKey()) {
                case "BUG":
                    typeStr = ReportTexts.GENERAL_BUGS;
                    break;
                case "VULNERABILITY":
                    typeStr = ReportTexts.GENERAL_SECURITY;
                    break;
                case "CODE_SMELL":
                    typeStr = ReportTexts.GENERAL_MAINTAINABILITY;
                    break;
                default:
                    typeStr = "";
                    break;
            }
            //issue type
            Paragraph header = Style.chapterLevel2().add(PropertyUtils.get(typeStr));
            //issue type severity
            List<Facet.FacetValue> severities = project.getSeverityMap().get(type);
            severities = PDFUtils.sortSeverity(severities);
            Table severityTable = new Table(severities.size())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER)
                    .setVerticalAlignment(VerticalAlignment.BOTTOM)
                    .setWidth(UnitValue.createPercentValue(80))
                    .setFixedLayout()
                    .setMarginLeft(50);
            for (Facet.FacetValue value : severities) {
                Cell cell = new Cell().setBorder(Border.NO_BORDER).setBorderRight(new GrooveBorder(0.1F));
                Paragraph valueP = Style.tableText()
                        .add(getSeverityImg(value.getVal()))
                        .add(PropertyUtils.get(PDFUtils.getSeverityText(value.getVal())))
                        .add(" : ")
                        .add(new Text(String.valueOf(value.getCount())).setFontColor(Style.MyColor.COLOR_STEEL_BLUE));
                cell.add(valueP);
                severityTable.addCell(cell);
            }
            document.add(header.add(severityTable));

            //issue details
            Table table = null;
            String file = "";

            for (Issue issue : issues) {
                String component = issue.getComponent();
                if (!file.equals(component) || table == null) {
                    if (table != null) {
                        document.add(table);
                    }
                    file = component;
                    table = new Table(new float[]{1F, 1.5F, 11, 1})
                            .setTextAlignment(TextAlignment.LEFT)
                            .setHorizontalAlignment(HorizontalAlignment.CENTER)
                            .setWidth(UnitValue.createPercentValue(100))
                            .setFixedLayout()
                            .setMarginBottom(10);
                    document.add(PDFUtils.issueFileName(file));
                }
                table.addCell(Style.issueCell().add(Style.tableText().add(PropertyUtils.get(typeStr))));
                table.addCell(Style.issueCell().add(
                        Style.tableText()
                                .add(getSeverityImg(issue.getSeverity()))
                                .add(" ")
                                .add(PropertyUtils.get(PDFUtils.getSeverityText(issue.getSeverity())))));
                table.addCell(Style.issueCell().add(Style.tableText().add(issue.getMessage())));
                table.addCell(Style.issueCell().add(Style.tableText().add(PDFUtils.lineStr(issue.getLine()))));
            }
            if (table != null) {
                document.add(table);
            }
        }
    }


    private URL getLogo() {
        return this.getClass().getResource("/static/img/sonarqube.png");
    }

    private URL getSvwLogo() {
        return this.getClass().getResource("/static/img/svw.png");
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
        ComponentService componentService = ComponentService.getInstance();
        //分析指标
        List<Measure> measures = componentService.getMeasures(this.projectKey, metricKeys());
        Map<String, Measure> measureMap = measures.stream().collect(Collectors.toMap(Measure::getMetric, measure -> measure));
        this.project.setMeasureMap(measureMap);
        //分析结果
        List<Analysis> analyses = componentService.getAnalysis(this.projectKey, 1, 1);
        if (analyses.size() > 0) {
            this.project.setAnalysis(analyses.get(0));
        }
        //issues
        List<SonarConstants.IssueType> types = getTypes();
        for (SonarConstants.IssueType type : types) {
            //severity
            List<Facet> facets = componentService.getFacets(this.projectKey, Arrays.asList("severities"), Arrays.asList(type.getKey()));
            if (facets != null && facets.size() > 0) {
                Facet facet = facets.get(0);
                if ("severities".equals(facet.getProperty())) {
                    project.getSeverityMap().put(type, facet.getValues());
                }
            }

            //issues detail
            int pageIndex = 0;
            int pageSize = 100;
            List<Issue> issues = new ArrayList<>();
            IssueDto issueDto;
            int total;
            do {
                issueDto = componentService.getIssueDto(this.projectKey, Arrays.asList("severities"), Arrays.asList(type.getKey()), pageSize, ++pageIndex);
                if (issueDto == null || issueDto.getTotal() == null) {
                    break;
                }
                if (issueDto.getIssues() != null && issueDto.getIssues().size() > 0) {
                    issues.addAll(issueDto.getIssues());
                }
                total = issueDto.getTotal();
            } while (pageIndex * pageSize < total);
            project.getIssueMap().put(type, issues);
        }
    }


    private List<String> metricKeys() {
        return Arrays.stream(MetricKeys.values()).map(MetricKeys::getKey).collect(Collectors.toList());
    }

    private List<SonarConstants.IssueType> getTypes() {
        if (this.issueTypes != null && issueTypes.size() > 0) {
            return issueTypes.stream().map(SonarConstants.IssueType::get)
                    .collect(Collectors.toList());
        }
        return Arrays.asList(SonarConstants.IssueType.values());
    }

    public PDFReporter setIssueTypes(List<String> types) {
        if (this.issueTypes == null) {
            issueTypes = new ArrayList<>();
        }
        issueTypes.clear();
        issueTypes.addAll(types);
        return this;
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


    private Map<SonarConstants.Severity, Image> severityImgMap;

    protected Image getSeverityImg(SonarConstants.Severity severity) {
        if (severityImgMap == null) {
            severityImgMap = new HashMap<>();
            Image image;
            for (SonarConstants.Severity s : SonarConstants.Severity.values()) {
                image = new Image(ImageDataFactory.create(Style.class.getResource("/static/img/severity_" + s.getKey().toLowerCase() + ".png")));
                severityImgMap.put(s, image);
            }
        }
        return severityImgMap.get(severity);
    }

}
