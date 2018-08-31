package org.sonarsource.plugins.report.support;

import lombok.extern.slf4j.Slf4j;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

@Slf4j
public class PropertySupport {

    private static Properties REPORT_PROPERTIES = new Properties();

    static {
        try {
            REPORT_PROPERTIES.load(new BufferedReader(new InputStreamReader(PropertyUtils.class.getResourceAsStream("/report.properties"), "UTF-8")));
        } catch (IOException e) {
            throw new ReportException("load report.properties error", e);
        }

        String language = REPORT_PROPERTIES.getProperty("report.language");
        String textFile;
        switch (language) {
            case "cn":
                textFile = "/report-texts-cn.properties";
                break;
            case "en":
                textFile = "/report-texts.properties";
                break;
            default:
                textFile = "/report-texts-cn.properties";
                log.info("wrong properties report.language set, use default [cn]");
                break;
        }
        Properties textProperties = new Properties();
        try {
            textProperties.load(new BufferedReader(new InputStreamReader(PropertyUtils.class.getResourceAsStream(textFile), "UTF-8")));
        } catch (IOException e) {
            throw new ReportException("load report text properties error, filename = " + textFile, e);
        }
        REPORT_PROPERTIES.putAll(textProperties);
    }


    public static Properties getReportProperties() {
        return REPORT_PROPERTIES;
    }


}
