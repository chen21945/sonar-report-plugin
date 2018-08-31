package org.sonarsource.plugins.report.constant;

public class WSConfig {

    /**
     * 服务端URL默认地址，可以在report.properties配置
     */
    public static final String DEFAULT_HOST_URL = "http://localhost/";
    /**
     * 报表接口
     */
    public static final String API_REPORTS = "api/reports";
    /**
     * 获取组件接口（项目信息）
     */
    public static final String API_COMPONENTS_SHOW = "api/components/show";
    /**
     * 项目分析结果
     */
    public static final String API_ANALYSIS_SEARCH = "api/project_analyses/search";
    /**
     * 项目分析指标
     */
    public static final String API_MEASURES = "api/measures/component";
    /**
     * 问题分析
     */
    public static final String API_ISSUES_SEARCH = "api/issues/search";

    /**
     * 设置查询
     */
    public static final String API_SETTINGS_VALUE = "api/settings/values";

}
