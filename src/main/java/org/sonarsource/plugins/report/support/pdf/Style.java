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
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.IOException;

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


    static class MyColor {
        private static final Color COLOR_BLUE = new DeviceRgb(100, 150, 190);
        private static final Color COLOR_STEEL_BLUE = new DeviceRgb(54, 100, 139);
    }


}
