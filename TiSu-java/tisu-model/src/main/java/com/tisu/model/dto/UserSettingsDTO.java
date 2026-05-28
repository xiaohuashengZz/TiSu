package com.tisu.model.dto;

import lombok.Data;

@Data
public class UserSettingsDTO {

    private String aiApiKey;

    private String aiModel;

    private String unitPreference;

    private Integer notificationEnabled;
}
