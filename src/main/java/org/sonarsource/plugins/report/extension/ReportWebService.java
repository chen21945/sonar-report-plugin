package org.sonarsource.plugins.report.extension;

import lombok.extern.slf4j.Slf4j;
import org.sonar.api.config.Configuration;
import org.sonar.api.server.ws.WebService;
import org.sonarsource.plugins.report.constant.WSConfig;
import org.sonarsource.plugins.report.support.PropertyUtils;
import org.sonarsource.plugins.report.support.handler.ReportRequestHandler;

/**
 * 自定义report web service
 */
@Slf4j
public class ReportWebService implements WebService {

    public ReportWebService(Configuration configuration) {
        String hostUrl = PropertyUtils.get("sonar.host.url");
        if (hostUrl.length() == 0) {
            String host = configuration.get("sonar.web.host").orElse("localhost");
            String port = configuration.get("sonar.web.port").orElse("9000");
            if (!"0.0.0.0".equals(host)) {
                hostUrl = "http://" + host + ("".equals(port) ? "" : ":" + port);
                PropertyUtils.set("sonar.host.url", hostUrl);
            }
        }
        log.info("server host = {}", hostUrl);
    }

    @Override
    public void define(Context context) {
        final NewController controller = context.createController(WSConfig.API_REPORTS);
        NewAction action = controller.createAction("pdf").setHandler(new ReportRequestHandler());
        action.createParam("key").setRequired(true);
        action.createParam("types").setRequired(false);
        action.createParam("severities").setRequired(false);
        action.createParam("sinceLeakPeriod").setRequired(false);
        controller.done();
    }

}
