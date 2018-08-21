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
package org.sonarsource.plugins.report.extension;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.Project;
import org.sonar.api.config.Configuration;
import org.sonar.api.platform.Server;
import org.sonarsource.plugins.report.constant.ReportConfigs;
import org.sonarsource.plugins.report.model.ReportConfiguration;
import org.sonarsource.plugins.report.service.ConfigService;
import org.sonarsource.plugins.report.support.exception.ReportException;
import org.sonarsource.plugins.report.support.pdf.PDFReporter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 报表任务扩展
 * 暂时没用
 * <p>
 * after the scanner sends the raw report to Compute Engine and  Compute Engine finishes analysis.
 */
@Slf4j
public class ReportTask implements PostProjectAnalysisTask {

    private final Server server;

    /**
     * global or default settings
     */
    private final Configuration config;

    public ReportTask(Configuration configuration, Server server) {
        this.config = configuration;
        this.server = server;
        log.info("server info, server={}", JSON.toJSONString(this.server));
    }

    @Override
    public void finished(ProjectAnalysis analysis) {
        Boolean globalEnabled = config.getBoolean(ReportConfigs.GLOBAL_ENABLED).orElse(false);
        if (!globalEnabled) {
            return;
        }
        Project project = analysis.getProject();
        if (project == null) {
            log.info("can not get project info, stop report task.");
            return;
        }
        ReportConfiguration rconfig = ConfigService.getInstance().getConfiguration(project.getKey());

        Boolean enabled = rconfig.getBoolean(ReportConfigs.ENABLED).orElse(false);
        Boolean newIssue = rconfig.getBoolean(ReportConfigs.NEW_ISSUE).orElse(false);
        List<String> issueTypes = Arrays.asList(rconfig.getStringArray(ReportConfigs.ISSUE_TYPES));
        List<String> severityTypes = Arrays.asList(rconfig.getStringArray(ReportConfigs.SEVERITY_TYPES));

        log.info("Configuration info, globalEnabled={},enabled={}, newIssue={},issueTypes={},severityTypes={}", globalEnabled, enabled, newIssue, issueTypes, severityTypes);

        if (Boolean.FALSE.equals(enabled)) {
            return;
        }

        log.info("Begin report task, projectKey={}================", project.getKey());

        PDFReporter reporter = new PDFReporter(project.getKey())
                .setSinceLeakPeriod(newIssue.toString())
                .setIssueTypes(issueTypes)
                .setSeverityTypes(severityTypes);
        try {
            File file = getReportFile(analysis.getProject());
            ByteArrayOutputStream stream = reporter.getReport();
            FileOutputStream fos = new FileOutputStream(file);
            stream.writeTo(fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            log.error("pdf report failed, error = {}", e.getMessage(), e);
        }

    }

    private File getReportFile(Project project) {
        String path = System.getProperty("user.dir") + "/tmp/";
        File dictionary = new File(path);
        if (!dictionary.exists()) {
            dictionary.mkdirs();
        }
        File file = new File(path + project.getKey() + ".pdf");
        if (file.exists()) {
            if (!file.delete()) {
                throw new ReportException("delete pdf report file failed, filename=" + file.getName());
            }
        }
        return file;
    }


}


