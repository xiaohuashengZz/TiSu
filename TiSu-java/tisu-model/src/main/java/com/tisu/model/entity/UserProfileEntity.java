package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_profile")
public class UserProfileEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String gender;
    private Integer age;
    private BigDecimal height;
    private BigDecimal currentWeight;
    private BigDecimal targetWeight;
    private BigDecimal bodyFat;
    private String fitnessGoal;
    private String activityLevel;
    private Integer dailyCalorieTarget;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
