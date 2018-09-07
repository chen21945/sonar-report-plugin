package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PropertyUtils {


    public static String get(String key) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return ObjectUtils.defaultIfNull(PropertySupport.getReportProperties().getProperty(key), "").trim();
    }


    public static void set(String key, String value) {
        if (StringUtils.isBlank(key)) {
            return;
        }
        PropertySupport.getReportProperties().setProperty(key, value);
    }

}
