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

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import lombok.extern.slf4j.Slf4j;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.IOException;
import java.net.URL;

@Slf4j
public class HeaderFooter implements IEventHandler {


    private Document document;

    private URL sonarLogo;
    private String projectNm;
    private URL svwLogo;
    private float height = 56;


    public HeaderFooter(Document document) {
        this.document = document;
    }


    @Override
    public void handleEvent(Event event) {
        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PageSize pageSize = pdfDoc.getDefaultPageSize();

        int pageIndex = pdfDoc.getPageNumber(page);
        if (pageIndex == 1) {
            return;
        }
        float width = (pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin()) / 4;
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);

        PdfFont font = Style.microsoftYaHei();
        //header
        canvas.addImage(ImageDataFactory.create(svwLogo),
                pageSize.getX() + document.getLeftMargin(),
                pageSize.getTop() - height,
                width,
                false);
        canvas.beginText()
                .setFillColor(ColorConstants.GRAY)
                .moveText(pageSize.getX() + document.getLeftMargin() + 2 * width - 10, pageSize.getTop() - height + 5)
                .setFontAndSize(font, 22)
                .showText(this.projectNm)
                .endText();
        canvas.addImage(ImageDataFactory.create(sonarLogo),
                pageSize.getX() + document.getLeftMargin() + width * 3,
                pageSize.getTop() - height,
                width,
                false);
        canvas.moveTo(document.getLeftMargin(), pageSize.getTop() - height)
                .lineTo(pageSize.getRight() - document.getRightMargin(), pageSize.getTop() - height)
                .stroke();

        //footer
        canvas.beginText()
                .setFontAndSize(font, 12)
                .moveText(pageSize.getWidth() / 2, pageSize.getY() + 10)
                .showText(String.valueOf(pdfDoc.getPageNumber(page) - 1))
                .endText();

        canvas.release();
    }


    public HeaderFooter setSonarLogo(URL sonarLogo) {
        this.sonarLogo = sonarLogo;
        return this;
    }

    public HeaderFooter setProjectNm(String projectNm) {
        this.projectNm = projectNm;
        return this;
    }

    public HeaderFooter setSvwLogo(URL svwLogo) {
        this.svwLogo = svwLogo;
        return this;
    }

    public float getHeight() {
        return this.height;
    }


}
