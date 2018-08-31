package org.sonarsource.plugins.report.extension;

import org.sonar.api.server.ws.WebService;
import org.sonarsource.plugins.report.constant.WSConfig;
import org.sonarsource.plugins.report.support.handler.ReportRequestHandler;

/**
 * 自定义report web service
 */
public class ReportWebService implements WebService {
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
