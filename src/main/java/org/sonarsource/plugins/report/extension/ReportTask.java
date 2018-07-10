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
import org.sonarsource.plugins.report.constant.ReportConfig;

/**
 * 报表任务扩展
 *
 * after the scanner sends the raw report to Compute Engine and  Compute Engine finishes analysis.
 */
@Slf4j
public class ReportTask implements PostProjectAnalysisTask {

    private final Server server;

    private final Configuration config;

    public ReportTask(Configuration configuration, Server server) {
        this.config = configuration;
        this.server = server;
    }

    @Override
    public void finished(ProjectAnalysis analysis) {
        log.info("Begin report task ---------------");
        Project project = analysis.getProject();
        if (project == null) {
            log.info("can not get project info, stop report task.");
        }

//        ReportGenerator generator = new ReportGenerator();
        log.info("server info, server={}", JSON.toJSONString(server));


        log.info("configuration info, enabled={}, type={} ", config.get(ReportConfig.ENABLED), config.get(ReportConfig.FILE_TYPE));
    }
}


