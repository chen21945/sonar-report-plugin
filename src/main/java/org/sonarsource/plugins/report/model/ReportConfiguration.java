package org.sonarsource.plugins.report.model;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.sonar.api.config.Configuration;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ReportConfiguration implements Configuration {

    private HashMap<String, String> data = new HashMap<>();

    private final String SEPARATOR = ",";

    public void addString(String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            log.warn("add configuration with empty, nothing added");
            return;
        }
        data.put(key, value);
    }

    public void addList(String key, List<String> values) {
        if (StringUtils.isBlank(key) || values == null || values.size() == 0) {
            log.warn("add configuration with empty, nothing added");
            return;
        }
        data.put(key, values.stream().collect(Collectors.joining(SEPARATOR)));
    }

    @Override
    public Optional<String> get(String s) {
        return Optional.ofNullable(this.data.get(validKey(s)));
    }

    @Override
    public boolean hasKey(String s) {
        return this.data.containsKey(validKey(s));
    }

    @Override
    public String[] getStringArray(String s) {
        String value = this.data.get(validKey(s));
        if (value == null) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
        } else {
            String[] strings = StringUtils.splitByWholeSeparator(value, SEPARATOR);
            String[] result = new String[strings.length];

            for (int index = 0; index < strings.length; ++index) {
                result[index] = StringUtils.trim(strings[index]);
            }
            return result;
        }
    }

    private String validKey(String key) {
        Objects.requireNonNull(key, "key can't be null");
        return key.trim();
    }
}
