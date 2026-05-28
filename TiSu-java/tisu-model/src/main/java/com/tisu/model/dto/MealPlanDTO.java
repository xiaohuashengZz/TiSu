package com.tisu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class MealPlanDTO {

    private String date;

    private String mealType;

    private List<Long> dishIds;
}
