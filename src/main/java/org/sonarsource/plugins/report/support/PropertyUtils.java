package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertyUtils {

    private static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(new BufferedReader(new InputStreamReader(PropertyUtils.class.getResourceAsStream("/report-texts-cn.properties"),"UTF-8")));
//            properties.load(new BufferedReader(new InputStreamReader(PropertyUtils.class.getResourceAsStream("/report-texts.properties"))));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("load properties error");
        }
    }

    public static String get(String key) {
        return ObjectUtils.defaultIfNull(properties.getProperty(key), "");
    }


}
