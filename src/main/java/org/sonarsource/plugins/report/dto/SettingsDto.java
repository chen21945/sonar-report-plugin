package org.sonarsource.plugins.report.dto;

import lombok.Getter;
import lombok.Setter;
import org.sonarsource.plugins.report.model.Settings;

import java.util.List;

@Getter
@Setter
public class SettingsDto {

    private List<Settings> settings;
}
