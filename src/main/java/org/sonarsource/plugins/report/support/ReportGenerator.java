package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.sonar.api.ce.posttask.Project;
import org.sonar.api.config.Configuration;
import org.sonarsource.plugins.report.support.pdf.PDFReporter;

@Slf4j
public class ReportGenerator {

    private Configuration configuration;
    private Project project;
    private String projectId;

    public ReportGenerator(Configuration configuration, Project project) {
        this.configuration = configuration;
        this.project = project;
    }

    public ReportGenerator(String projectId){
        this.projectId = projectId;
    }

    public void execute() {
        PDFReporter reporter = null;
//            ByteArrayOutputStream baos = reporter.getReport();
//            FileOutputStream fos = new FileOutputStream(new File(path));
//            baos.writeTo(fos);
//            fos.flush();
//            fos.close();
//            LOG.info("PDF report generated (see " + sonarProjectId.replace(':', '-') + ".pdf on build output directory)");
    }
}
