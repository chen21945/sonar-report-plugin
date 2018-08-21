package org.sonarsource.plugins.report.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.sonarsource.plugins.report.constant.ReportConfigs;
import org.sonarsource.plugins.report.constant.WSConfig;
import org.sonarsource.plugins.report.dto.SettingsDto;
import org.sonarsource.plugins.report.model.ReportConfiguration;
import org.sonarsource.plugins.report.support.RequestManager;
import org.sonarsource.plugins.report.support.exception.ReportException;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * 获取配置信息
 */
public class ConfigService extends BaseService {


    /**
     * 获取项目配置信息
     *
     * @param projectKey
     * @return
     */
    public ReportConfiguration getConfiguration(String projectKey) {
        if (StringUtils.isBlank(projectKey)) {
            return null;
        }
        ReportConfiguration configuration = new ReportConfiguration();
        String url = getUrl(WSConfig.API_SETTINGS_VALUE,
                ImmutableMap.of(
                        "component", projectKey,
                        "keys", ReportConfigs.getConfigKeys().stream().collect(Collectors.joining(","))));
        RequestManager manager = RequestManager.getInstance();
        try {
            String result = manager.get(url);
            SettingsDto dto = JSON.parseObject(result, SettingsDto.class);
            if (dto != null && dto.getSettings() != null) {
                dto.getSettings().forEach(
                        settings -> {
                            if (settings.getValue() != null) {
                                configuration.addString(settings.getKey(), settings.getValue());
                            } else if (settings.getValues() != null) {
                                configuration.addList(settings.getKey(), settings.getValues());
                            }
                        }
                );
            }
        } catch (IOException e) {
            throw new ReportException("error get project with key [{" + projectKey + "}]", e);
        }
        return configuration;
    }


    private static ConfigService configService;

    public static synchronized ConfigService getInstance() {
        if (configService == null) {
            configService = new ConfigService();
        }
        return configService;
    }
}
