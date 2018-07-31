package org.sonarsource.plugins.report.service;

import org.apache.commons.lang3.StringUtils;
import org.sonarsource.plugins.report.constant.WSConfig;

import java.util.Map;

public class BaseService {

    String baseUrl;

    protected String getBaseUrl() {
        if (StringUtils.isNotBlank(baseUrl)) {
            return baseUrl;
        }
        return WSConfig.HOST_URL;
    }

    public String getUrl(String api, Map<String, Object> params) {
        if (api == null) {
            return "";
        }
        String baseUrl = getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl = baseUrl + "/";
        }
        while (api.startsWith("/")) {
            api = api.substring(1);
        }
        String url = baseUrl + api;

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
