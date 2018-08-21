package org.sonarsource.plugins.report.constant;

public class WSConfig {

    /**
     * 服务端URL地址
     * 此处使用80端口，根据需要修改
     */
    public static final String HOST_URL = "http://localhost/";

    /**
     * 查询用token
     */
    public static final String TOKEN = "22858fd738f365bd57df4a6fc3602dbcaa887eb9";
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
