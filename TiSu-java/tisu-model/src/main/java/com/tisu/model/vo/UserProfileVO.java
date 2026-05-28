package com.tisu.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserProfileVO {

    private Long id;

    private String username;

    private String nickname;

    private String email;

    private String phone;

    private String avatar;

    private String gender;

    private Integer age;

    private BigDecimal height;

    private BigDecimal currentWeight;

    private BigDecimal targetWeight;

    private BigDecimal bodyFat;

    private String fitnessGoal;

    private String activityLevel;

    private Integer dailyCalorieTarget;
}
