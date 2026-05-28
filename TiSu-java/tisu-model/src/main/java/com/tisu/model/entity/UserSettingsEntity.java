package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("user_settings")
public class UserSettingsEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String aiApiKey;
    private String aiModel;
    private String unitPreference;
    private Integer notificationEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
