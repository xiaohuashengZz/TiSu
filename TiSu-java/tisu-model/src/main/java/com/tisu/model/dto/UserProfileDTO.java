package com.tisu.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserProfileDTO {

    private String gender;

    private Integer age;

    private BigDecimal height;

    private BigDecimal currentWeight;

    private BigDecimal targetWeight;

    private BigDecimal bodyFat;

    private String fitnessGoal;

    private String activityLevel;
}
