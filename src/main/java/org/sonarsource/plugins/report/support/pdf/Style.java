/*
 * SonarQube PDF Report
 * Copyright (C) 2010 klicap - ingenieria del puzle
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonarsource.plugins.report.support.pdf;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.GrooveBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.sonarsource.plugins.report.constant.SonarConstants;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Style {

    public class FontType {
        static final String MICROSOFT_YAHEI = "static/font/MicrosoftYaHei.ttf";

    }

    private static PdfFont microsoftYaHei;
    private static PdfFont stSong;

    public static PdfFont microsoftYaHei() {
        if (microsoftYaHei == null) {
            try {
                microsoftYaHei = PdfFontFactory.createFont(FontType.MICROSOFT_YAHEI, PdfEncodings.IDENTITY_H, true);
            } catch (IOException e) {
                log.error("load microsoft YaHei font failed", e);
                throw new ReportException("load  microsoft YaHei font failed");
            }
        }
        return microsoftYaHei;
    }

    public static PdfFont stSong() {
        if (stSong == null) {
            try {
                stSong = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H", true);
            } catch (IOException e) {
                log.error("load ST-Song font failed", e);
                throw new ReportException("load ST-Song font failed ");
            }
        }
        return stSong;
    }

    public static Paragraph chapterLevel1() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setBold()
                .setFontSize(18)
                .setMarginBottom(12);
    }

    public static Paragraph chapterLevel2() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setBold()
                .setFontSize(16)
                .setMarginBottom(8);
    }

    public static Paragraph text() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setFontSize(12)
                .setFirstLineIndent(24);
    }

    public static Paragraph smallText() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setFontSize(10)
                .setFontColor(ColorConstants.GRAY);
    }

    public static Paragraph tableCellLarge() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setFontSize(20)
                .setFontColor(MyColor.COLOR_STEEL_BLUE)
                .setBold()
                .setUnderline(MyColor.COLOR_STEEL_BLUE, 0.2F, 0, 0, -0.45F, PdfCanvasConstants.LineCapStyle.BUTT);
    }

    public static Paragraph tableCellMiddle() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setFontSize(12)
                .setFontColor(MyColor.COLOR_STEEL_BLUE);
    }

    public static Paragraph tableText() {
        return new Paragraph()
                .setFont(microsoftYaHei())
                .setFontSize(10);
    }

    public static Cell metricCell(String value, String text) {
        return metricCell(value, text, Border.NO_BORDER);
    }

    public static Cell metricCellBorderedWithImg(String value, String text) {
        return metricCell(value, text, new GrooveBorder(0.1F));
    }

    public static Cell metricCellBorderedWithImg(String value, String text, Image image) {
        Paragraph ptext = Style.tableCellLarge().add(value);
        Paragraph pvalue = Style.tableCellMiddle().setFontColor(ColorConstants.DARK_GRAY)
                .add(image)
                .add(" ")
                .add(text);
        return new Cell().add(ptext).add(pvalue).setBorder(new GrooveBorder(0.1F));
    }


    public static Cell issueCell() {
        return new Cell()
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new GrooveBorder(0.1F));
    }


    /**
     * 指标Cell
     * 上数值，下名称
     *
     * @param value
     * @param text
     * @param border
     * @return
     */
    private static Cell metricCell(String value, String text, Border border) {
        Paragraph ptext = Style.tableCellLarge().add(value);
        Paragraph pvalue = Style.tableCellMiddle().setFontColor(ColorConstants.DARK_GRAY).add(text);
        return new Cell().add(ptext).add(pvalue).setBorder(border);
    }


    public static class MyColor {
        public static final Color COLOR_BLUE = new DeviceRgb(100, 150, 190);
        public static final Color COLOR_STEEL_BLUE = new DeviceRgb(54, 100, 139);
        public static final Color COLOR_SLATE_GREY = new DeviceRgb(198, 226, 255);
    }


    private static Map<SonarConstants.Severity, Image> severityImgMap;

    public static Image getSeverityImg(SonarConstants.Severity severity) {
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
