package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertyUtils {

    public static Properties properties;

    static {
        try {
            properties = new Properties();
            properties.load(PropertyUtils.class.getResourceAsStream("/report-texts.properties"));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("load properties error");
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

}
