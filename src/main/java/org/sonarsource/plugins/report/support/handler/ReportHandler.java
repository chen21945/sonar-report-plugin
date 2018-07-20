package org.sonarsource.plugins.report.support.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonarsource.plugins.report.support.pdf.PDFReporter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * report controller action
 */
@Slf4j
public class ReportHandler implements RequestHandler {

    @Override
    public void handle(Request request, Response response) throws Exception {
        log.info("report controller begin");
        String projectKey = request.mandatoryParam("key");
        String issueTypes = request.hasParam("types") ? request.getParam("types").getValue() : null;
        log.info("key ={},issueTypes={} ", projectKey, issueTypes);

        PDFReporter reporter = new PDFReporter(projectKey);
        if (StringUtils.isNotBlank(issueTypes)) {
            reporter.setIssueTypes(Arrays.asList(issueTypes.split(",")));
        }

        ByteArrayOutputStream stream = reporter.getReport();

        response.stream().setMediaType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment;filename=\"" + reporter.project().getName() + "_" + reporter.project().getVersion() + ".pdf\"");
        OutputStream output = response.stream().output();
        log.info("begin to write");
        stream.writeTo(output);
        stream.close();
        output.close();
        log.info("report controller end");
    }

}
