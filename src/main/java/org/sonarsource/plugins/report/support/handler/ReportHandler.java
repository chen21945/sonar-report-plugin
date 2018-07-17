package org.sonarsource.plugins.report.support.handler;

import lombok.extern.slf4j.Slf4j;
import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.RequestHandler;
import org.sonar.api.server.ws.Response;
import org.sonarsource.plugins.report.support.pdf.PDFReporter;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * report controller action
 */
@Slf4j
public class ReportHandler implements RequestHandler {

    @Override
    public void handle(Request request, Response response) throws Exception {
        log.info("report controller begin");
        log.info("path={}", request.getPath());
        String projectKey = request.mandatoryParam("key");
        log.info("key ={} ", projectKey);

        PDFReporter reporter = new PDFReporter(projectKey);
        ByteArrayOutputStream stream = reporter.getReport();
        log.info("get report success");
        response.stream().setMediaType("application/octet-stream");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + reporter.project().getName() + "_" + reporter.project().getVersion() + ".pdf\"");
        OutputStream output = response.stream().output();
        log.info("begin to write");
        stream.writeTo(output);
        output.close();
        stream.close();
        log.info("report controller end");
    }

}
