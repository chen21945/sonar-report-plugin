package org.sonarsource.plugins.report.extension;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.Page.Scope;
import org.sonar.api.web.page.PageDefinition;

/**
 * 页面定义扩展
 * 通过Scope区分页面位置， Component表示项目页面，Global是全局管理设置
 * <p>
 * 其中Page中的report指的是pluginKey，在pom.xml中配置
 */
public class ReportPageDefinition implements PageDefinition {

    @Override
    public void define(Context context) {
        context.addPage(Page.builder("reports/report_page_4_project")
                .setName("PDF Report")
                .setScope(Scope.COMPONENT).build());
    }
}
