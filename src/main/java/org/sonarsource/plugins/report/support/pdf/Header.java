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

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.ce.posttask.Project;

import java.io.IOException;
import java.net.URL;

public class Header extends PdfPageEventHelper {

    private static final Logger LOG = LoggerFactory.getLogger(Header.class);

    private URL logo;
    private Project project;

    public Header(final URL logo, final Project project) {
        this.logo = logo;
        this.project = project;
    }

    @Override
    public void onEndPage(final PdfWriter writer, final Document document) {
        try {
            Image logoImage = Image.getInstance(logo);
            Rectangle page = document.getPageSize();
            PdfPTable head = new PdfPTable(4);
            head.getDefaultCell().setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
            head.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            head.addCell(logoImage);
            Phrase projectName = new Phrase(project.getName(), FontFactory.getFont(
                    FontFactory.COURIER, 12, Font.NORMAL, BaseColor.GRAY));
            Phrase phrase = new Phrase("Sonar PDF Report", FontFactory.getFont(
                    FontFactory.COURIER, 12, Font.NORMAL, BaseColor.GRAY));
            head.getDefaultCell().setColspan(2);
            head.addCell(phrase);
            head.getDefaultCell().setColspan(1);
            head.addCell(projectName);
            head.setTotalWidth(page.getWidth() - document.leftMargin()
                    - document.rightMargin());
            head.writeSelectedRows(0, -1, document.leftMargin(),
                    page.getHeight() - 20, writer.getDirectContent());
            head.setSpacingAfter(10);
        } catch (BadElementException | IOException e) {
            LOG.error("Can not generate PDF header", e);
        }
    }

}
