package com.tisu.model.vo;

import lombok.Data;

@Data
public class UserSettingsVO {

    private String aiApiKey;

    private String aiModel;

    private String unitPreference;

    private Integer notificationEnabled;
}
