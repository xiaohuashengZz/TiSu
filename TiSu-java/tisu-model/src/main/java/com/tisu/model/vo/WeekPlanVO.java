package com.tisu.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class WeekPlanVO {

    private String day;

    private List<MealVO> meals;

    private Integer totalCalories;
}
