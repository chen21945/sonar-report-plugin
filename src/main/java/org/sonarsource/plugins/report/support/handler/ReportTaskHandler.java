package org.sonarsource.plugins.report.support.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.ce.posttask.Project;
import org.sonar.api.config.Configuration;
import org.sonar.api.config.EmailSettings;
import org.sonar.api.platform.Server;
import org.sonarsource.plugins.report.constant.ReportConfigs;
import org.sonarsource.plugins.report.model.ReportConfiguration;
import org.sonarsource.plugins.report.service.ConfigService;
import org.sonarsource.plugins.report.support.EmailSupport;
import org.sonarsource.plugins.report.support.PropertyUtils;
import org.sonarsource.plugins.report.support.exception.ReportException;
import org.sonarsource.plugins.report.support.pdf.PDFReporter;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ReportTaskHandler {

    /**
     * global or default settings
     */
    private final Configuration config;
    private final EmailSettings emailSettings;
    private final Server server;

    public ReportTaskHandler(Configuration configuration, Server server, EmailSettings emailSettings) {
        this.config = configuration;
        this.server = server;
        this.emailSettings = emailSettings;
    }

    public void genReport(Project project) {
        Boolean globalEnabled = config.getBoolean(ReportConfigs.GLOBAL_ENABLED).orElse(false);
        if (!globalEnabled) {
            return;
        }
        if (project == null) {
            log.info("can not get project info, stop report task.");
            return;
        }
        ReportConfiguration rconfig = ConfigService.getInstance().getConfiguration(project.getKey());

        Boolean enabled = rconfig.getBoolean(ReportConfigs.ENABLED).orElse(false);
        Boolean newIssue = rconfig.getBoolean(ReportConfigs.NEW_ISSUE).orElse(false);
        List<String> issueTypes = Arrays.asList(rconfig.getStringArray(ReportConfigs.ISSUE_TYPES));
        List<String> severityTypes = Arrays.asList(rconfig.getStringArray(ReportConfigs.SEVERITY_TYPES));
        String emails = rconfig.get(ReportConfigs.TO_EMAILS).orElse(null);

        log.info("Configuration info, globalEnabled={},enabled={}, newIssue={},issueTypes={},severityTypes={}, emails={}", globalEnabled, enabled, newIssue, issueTypes, severityTypes, emails);

        if (Boolean.FALSE.equals(enabled) || StringUtils.isBlank(emails)) {
            return;
        }

        log.info("Begin report task, projectKey={}================", project.getKey());

        PDFReporter reporter = new PDFReporter(project.getKey())
                .setSinceLeakPeriod(newIssue.toString())
                .setIssueTypes(issueTypes)
                .setSeverityTypes(severityTypes);
        try {
            File file = getReportFile(project);
            ByteArrayOutputStream stream = reporter.getReport();
            FileOutputStream fos = new FileOutputStream(file);
            stream.writeTo(fos);
            fos.flush();
            fos.close();
            //send emails notification
            sendEmail(emails, file.getAbsolutePath());
        } catch (IOException e) {
            log.error("pdf report failed, error = {}", e.getMessage(), e);
        }
        log.info("Report task end ===============");
    }


    private File getReportFile(Project project) {
        String tmpPath = PropertyUtils.get("report.tmp.dir");
        if (StringUtils.isEmpty(tmpPath)) {
            tmpPath = "/tmp/";
        }
        File dictionary = Paths.get(System.getProperty("user.dir"), tmpPath).toFile();
        if (!dictionary.exists()) {
            dictionary.mkdirs();
        }
        File file = Paths.get(dictionary.getAbsolutePath(), project.getKey() + ".pdf").toFile();
        if (file.exists()) {
            if (!file.delete()) {
                throw new ReportException("delete pdf report file failed, filename=" + file.getName());
            }
        }
        return file;
    }


    private void sendEmail(String toEmails, String fileName) {
        if (emailSettings == null) {
            log.error("send sonarqube analysis report email failed,can not get email setting info");
            return;
        }
        try {
            EmailSupport.sendEmailWithAttachments(
                    emailSettings.getSmtpHost(),
                    emailSettings.getSmtpPort(),
                    emailSettings.getSmtpUsername(),
                    emailSettings.getSmtpPassword(),
                    emailSettings.getFromName(),
                    emailSettings.getFrom(),
                    StringUtils.splitByWholeSeparator(toEmails, ","),
                    PropertyUtils.get("report.email.subject"),
                    PropertyUtils.get("report.email.message"),
                    new String[]{fileName}
            );
        } catch (MessagingException e) {
            log.error("send sonarqube analysis report email failed, message={}", e.getMessage(), e);
        }
    }
}
