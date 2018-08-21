package org.sonarsource.plugins.report.extension;

import lombok.extern.slf4j.Slf4j;
import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.config.Configuration;
import org.sonarsource.plugins.report.constant.Categorys;

/**
 * PostJob 报表任务扩展
 * 暂时没用
 */
@Slf4j
public class ReportPostJob implements PostJob {


    @Override
    public void describe(PostJobDescriptor postJobDescriptor) {
        postJobDescriptor.name(Categorys.PDF_REPORT.getCode());
    }

    @Override
    public void execute(final PostJobContext postJobContext) {
        log.info("------------------------");
        Configuration config = postJobContext.config();
        log.info("post job config, enabled={}, issueTypes={},newIssue={} ", config.getBoolean("sonar.report.enabled"), config.get("sonar.report.issueTypes"), config.getBoolean("sonar.report.newIssue"));
    }
}
