package org.sonarsource.plugins.report.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.PageDefinition;

/**
 * 页面定义
 * 通过Scope区分页面位置， Component表示项目页面，Global是全局管理设置
 */
public class MyPluginPageDefinition implements PageDefinition {

  @Override
  public void define(Context context) {
//    context
//      .addPage(Page.builder("report/custom_page_4_project")
//        .setName("Custom Project Page (Pure JS)")
//        .setScope(Scope.COMPONENT).build())
//      .addPage(Page.builder("report/measures_history")
//        .setName("Custom Project Page using ReactJS")
//        .setScope(Scope.COMPONENT).build())
//
//      .addPage(Page.builder("report/custom_page_4_admin")
//        .setName("Custom Admin Page")
//        .setScope(Scope.GLOBAL)
//        .setAdmin(Boolean.TRUE).build())
//      .addPage(Page.builder("report/sanity_check")
//        .setName("Custom Admin Page Sanity Check")
//        .setScope(Scope.GLOBAL)
//        .setAdmin(Boolean.TRUE).build())
//
//      .addPage(Page.builder("report/custom_page_global")
//        .setName("Custom Global Page")
//        .setScope(Scope.GLOBAL).build());
  }
}
