/*
 * Example Plugin for SonarQube
 * Copyright (C) 2009-2016 SonarSource SA
 * mailto:contact AT sonarsource DOT com
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
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonarsource.plugins.report;

import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonarsource.plugins.report.constant.Categorys;
import org.sonarsource.plugins.report.constant.ReportConfig;
import org.sonarsource.plugins.report.extension.ReportPageDefinition;
import org.sonarsource.plugins.report.extension.ReportPostJob;
import org.sonarsource.plugins.report.extension.ReportTask;
import org.sonarsource.plugins.report.extension.ReportWebService;

import java.util.Arrays;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class ReportPlugin implements Plugin {

    @Override
    public void define(Context context) {

        // tutorial on hooks
        // http://docs.sonarqube.org/display/DEV/Adding+Hooks
//        context.addExtensions(ReportPostJob.class, ReportTask.class);
        context.addExtension(ReportWebService.class);
        // tutorial on web extensions
        context.addExtension(ReportPageDefinition.class);

        context.addExtensions(Arrays.asList(
                PropertyDefinition.builder(ReportConfig.ENABLED)
                        .name("Enabled")
                        .description("Whether to automatically generate PDF report")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.BOOLEAN)
                        .defaultValue("true")
                        .build(),
                PropertyDefinition.builder(ReportConfig.FILE_TYPE)
                        .name("File Type")
                        .description("the file type to generate")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.SINGLE_SELECT_LIST)
                        .options("pdf", "excel")
                        .defaultValue("pdf")
                        .build()));
    }
}
