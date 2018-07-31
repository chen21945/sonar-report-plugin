package org.sonarsource.plugins.report.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.navigation.PdfDestination;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.hyphenation.HyphenationConfig;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.ParagraphRenderer;
import org.sonarsource.plugins.report.support.pdf.Style;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class TableOfContents {
    public static final String SRC = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\jekyll_hyde.txt";
    public static final String DEST = "C:\\Users\\yangchengang2.CNSVWSH00\\Desktop\\table_of_contents.pdf";

    public static void main(String args[]) throws IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new TableOfContents().createPdf(DEST);
    }

    public void createPdf(String dest) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
        pdf.getCatalog().setPageMode(PdfName.UseOutlines);

        PdfFont font = PdfFontFactory.createFont(Style.FontType.SOURCE_HAN_SANS, PdfEncodings.IDENTITY_H, true);
        PdfFont bold = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", true);

        Document document = new Document(pdf);
        document.setTextAlignment(TextAlignment.JUSTIFIED)
                .setHyphenation(new HyphenationConfig("en", "uk", 3, 3))
                .setFont(font)
                .setFontSize(11);

        // parse text to PDF
        BufferedReader br = new BufferedReader(new FileReader(SRC));
        String name, line;
        Paragraph p;
        boolean title = true;
        int counter = 0;
        PdfOutline outline = null;
        List<AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>>> toc = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            p = new Paragraph(line);
            p.setKeepTogether(true);
            if (title) {
                name = String.format("title%02d", counter++);
                outline = createOutline(outline, pdf, line, name);
                AbstractMap.SimpleEntry<String, Integer> titlePage
                        = new AbstractMap.SimpleEntry(line, pdf.getNumberOfPages());
                p.setFont(bold).setFontSize(12)
                        .setKeepWithNext(true)
                        .setDestination(name)
                        .setNextRenderer(new UpdatePageRenderer(p, titlePage))
                ;
                title = false;
                document.add(p);
                toc.add(new AbstractMap.SimpleEntry(name, titlePage));
            } else {
                p.setFirstLineIndent(36);
                if (line.isEmpty()) {
                    p.setMarginBottom(12);
                    title = true;
                } else {
                    p.setMarginBottom(0);
                }
                document.add(p);
            }
        }
        document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

        // create table of contents
//        int startToc = pdf.getNumberOfPages();
//        p = new Paragraph().setFont(bold)
//                .add("Table of Contents").setDestination("toc");
//        document.add(p);
//        toc.remove(0);
//        List<TabStop> tabstops = new ArrayList();
//        tabstops.add(new TabStop(580, TabAlignment.RIGHT, new DottedLine()));
//        for (AbstractMap.SimpleEntry<String, AbstractMap.SimpleEntry<String, Integer>> entry : toc) {
//            AbstractMap.SimpleEntry<String, Integer> text = entry.getValue();
//            p = new Paragraph()
//                    .addTabStops(tabstops)
//                    .add(text.getKey())
//                    .add(new Tab())
//                    .add(String.valueOf(text.getValue()))
//                    .setAction(PdfAction.createGoTo(entry.getKey()));
//            document.add(p);
//        }
//        int tocPages = pdf.getNumberOfPages() - startToc;

//        // reorder pages
//        PdfPage page;
//        for (int i = 0; i <= tocPages; i++) {
//            page = pdf.removePage(startToc + i);
//            pdf.addPage(i + 1, page);
//        }
//
//        // add page labels
//        pdf.getPage(1)
//                .setPageLabel(PageLabelNumberingStyleConstants.LOWERCASE_ROMAN_NUMERALS, null, 1);
//        pdf.getPage(2 + tocPages)
//                .setPageLabel(PageLabelNumberingStyleConstants.DECIMAL_ARABIC_NUMERALS, null, 1);

        document.close();
    }

    protected class UpdatePageRenderer extends ParagraphRenderer {
        protected AbstractMap.SimpleEntry<String, Integer> entry;

        public UpdatePageRenderer(Paragraph modelElement, AbstractMap.SimpleEntry<String, Integer> entry) {
            super(modelElement);
            this.entry = entry;
        }

        @Override
        public LayoutResult layout(LayoutContext layoutContext) {
            LayoutResult result = super.layout(layoutContext);
            entry.setValue(layoutContext.getArea().getPageNumber());
            return result;
        }
    }

    public PdfOutline createOutline(PdfOutline outline, PdfDocument pdf, String title, String name) {
        if (outline == null) {
            outline = pdf.getOutlines(false);
            outline = outline.addOutline(title);
            outline.addDestination(PdfDestination.makeDestination(new PdfString(name)));
            return outline;
        }
        PdfOutline kid = outline.addOutline(title);
        kid.addDestination(PdfDestination.makeDestination(new PdfString(name)));
        return outline;
    }
}