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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.EmailException;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.Project;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.EmailSettings;
import org.sonar.api.platform.Server;
import org.sonarsource.plugins.report.constant.ReportConfigs;
import org.sonarsource.plugins.report.model.ReportConfiguration;
import org.sonarsource.plugins.report.service.ConfigService;
import org.sonarsource.plugins.report.support.CommonEmailSupport;
import org.sonarsource.plugins.report.support.PropertyUtils;

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

    private final EmailSettings emailSettings;

    public ReportTask(Configuration configuration, Server server, EmailSettings emailSettings) {
        this.config = configuration;
        this.server = server;
        this.emailSettings = emailSettings;
    }

    @Override
    public void finished(ProjectAnalysis analysis) {
        //check global enabled config and email settings
        Boolean globalEnabled = config.getBoolean(ReportConfigs.GLOBAL_ENABLED).orElse(false);
        if (!globalEnabled) {
            return;
        }
        if (!checkEmailSettings(emailSettings)) {
            log.error("send sonarqube notification email failed, wrong email setting info");
            return;
        }

        Project project = analysis.getProject();
        if (project == null) {
            log.info("can not get project info, stop report task.");
            return;
        }
        ReportConfiguration rconfig = ConfigService.getInstance().getConfiguration(project.getKey());
        Boolean enabled = rconfig.getBoolean(ReportConfigs.ENABLED).orElse(false);
        String emails = rconfig.get(ReportConfigs.TO_EMAILS).orElse(null);
        log.info("Configuration info, globalEnabled={},enabled={},emails={}", globalEnabled, enabled, emails);

        if (Boolean.FALSE.equals(enabled) || StringUtils.isBlank(emails)) {
            return;
        }

        try {
            CommonEmailSupport.sendSimpleEmail(
                    emailSettings.getSmtpHost(),
                    emailSettings.getSmtpUsername(),
                    emailSettings.getSmtpPassword(),
                    emailSettings.getFromName(),
                    emailSettings.getFrom(),
                    StringUtils.splitByWholeSeparator(emails, ","),
                    PropertyUtils.get("report.email.subject"),
                    PropertyUtils.get("report.email.message") + "\n\n" + getUrl(project)
            );
        } catch (EmailException e) {
            log.error("send sonarqube notification failed, message={}", e.getMessage(), e);
        }
    }


    private boolean checkEmailSettings(EmailSettings emailSettings) {
        if (emailSettings == null) {
            return false;
        }
        if (emailSettings.getSmtpHost() == null) {
            return false;
        }
        if (emailSettings.getFrom() == null) {
            return false;
        }
        return true;
    }

    private String getUrl(Project project) {
        String hostUrl = PropertyUtils.get("sonar.host.url");
        if (hostUrl.length() == 0) {
            hostUrl = server.getPublicRootUrl();
            if (hostUrl.contains("localhost") || hostUrl.contains("0.0.0.0")) {
                return "";
            }
        }
        if (!hostUrl.endsWith("/")) {
            hostUrl = hostUrl + "/";
        }
        return hostUrl + "dashboard" + "?id=" + project.getKey();
    }


}



