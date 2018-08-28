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
import org.sonar.api.resources.Qualifiers;
import org.sonarsource.plugins.report.constant.Categorys;
import org.sonarsource.plugins.report.extension.ReportPageDefinition;
import org.sonarsource.plugins.report.extension.ReportTask;
import org.sonarsource.plugins.report.extension.ReportWebService;

import java.util.Arrays;

import static org.sonarsource.plugins.report.constant.ReportConfigs.*;

/**
 * This class is the entry point for all extensions. It is referenced in pom.xml.
 */
public class ReportPlugin implements Plugin {

    @Override
    public void define(Context context) {

//        tutorial on hooks
//        http://docs.sonarqube.org/display/DEV/Adding+Hooks
        context.addExtension(ReportTask.class);
        context.addExtension(ReportWebService.class);
        // tutorial on web extensions
        context.addExtension(ReportPageDefinition.class);

        context.addExtensions(Arrays.asList(
                PropertyDefinition.builder(GLOBAL_ENABLED)
                        .name("Enabled")
                        .description("Whether to automatically generate PDF report")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.BOOLEAN)
                        .defaultValue("false")
                        .index(0)
                        .build(),
                PropertyDefinition.builder(ENABLED)
                        .name("Enabled")
                        .description("Whether to automatically generate PDF report")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.BOOLEAN)
                        .defaultValue("true")
                        .onlyOnQualifiers(Qualifiers.PROJECT)
                        .index(0)
                        .build(),
                PropertyDefinition.builder(TO_EMAILS)
                        .name("To Emails")
                        .description("Receive email notifications")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.STRING)
                        .onlyOnQualifiers(Qualifiers.PROJECT)
                        .index(1)
                        .build(),
                PropertyDefinition.builder(ISSUE_TYPES)
                        .name("Filter Issue Types")
                        .description("Filter issue types\n (Bug,Vulnerability,Code_Smell)")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.SINGLE_SELECT_LIST)
                        .options("Bug", "Vulnerability", "Code_Smell")
                        .multiValues(true)
                        .defaultValue("Bug")
                        .onlyOnQualifiers(Qualifiers.PROJECT)
                        .index(2)
                        .build(),
                PropertyDefinition.builder(SEVERITY_TYPES)
                        .name("Filter Severity Types")
                        .description("Filter severity types\n (BLOCKER,CRITICAL,MAJOR,MINOR,INFO)")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.SINGLE_SELECT_LIST)
                        .options("BLOCKER", "CRITICAL", "MAJOR", "MINOR", "INFO")
                        .multiValues(true)
                        .defaultValue("BLOCKER,CRITICAL,MAJOR")
                        .onlyOnQualifiers(Qualifiers.PROJECT)
                        .index(3)
                        .build(),
                PropertyDefinition.builder(NEW_ISSUE)
                        .name("New Issue")
                        .description("Only export new issues?")
                        .category(Categorys.PDF_REPORT.getCode())
                        .type(PropertyType.BOOLEAN)
                        .defaultValue("false")
                        .onlyOnQualifiers(Qualifiers.PROJECT)
                        .index(4)
                        .build())
        );
    }
}
