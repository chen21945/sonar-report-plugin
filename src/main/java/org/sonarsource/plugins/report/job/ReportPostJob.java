package org.sonarsource.plugins.report.job;

import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;

/**
 * 报表任务
 */
public class ReportPostJob implements PostJob {


    @Override
    public void describe(PostJobDescriptor postJobDescriptor) {
        postJobDescriptor.name("Report");
    }

    @Override
    public void execute(PostJobContext postJobContext) {

    }
}
