package org.sonarsource.plugins.report.service;

import org.apache.commons.lang3.StringUtils;
import org.sonarsource.plugins.report.constant.ReportConfig;

import java.util.Map;

public class BaseService {

    String baseUrl;

    protected String getBaseUrl() {
        if (StringUtils.isNotBlank(baseUrl)) {
            return baseUrl;
        }
        return ReportConfig.WSConfig.HOST_URL;
    }

    public String getUrl(String api, Map<String, Object> params) {
        String url = getBaseUrl() + api;
        if (params != null && params.size() > 0) {
            StringBuilder paramSb = new StringBuilder();
            params.keySet().forEach(
                    key -> paramSb.append("&").append(key).append("=").append(params.get(key))
            );
            url += "?" + paramSb.substring(1);
        }
        return url;
    }

    protected String getUrl(String api) {
        return getUrl(api, null);
    }


}
