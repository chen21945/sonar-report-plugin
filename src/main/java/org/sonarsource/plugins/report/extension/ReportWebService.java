package org.sonarsource.plugins.report.extension;

import org.sonar.api.server.ws.WebService;
import org.sonarsource.plugins.report.constant.ReportConfig;
import org.sonarsource.plugins.report.support.handler.ReportHandler;

/**
 * 自定义report web service
 */
public class ReportWebService implements WebService {
    @Override
    public void define(Context context) {
        final NewController controller = context.createController(ReportConfig.WSConfig.API_REPORTS);
        NewAction action = controller.createAction("pdf").setHandler(new ReportHandler());
        action.createParam("key").setRequired(true);
        action.createParam("types").setRequired(false);
        controller.done();
    }
}
