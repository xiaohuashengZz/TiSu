package com.tisu.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodDTO {

    private String name;

    private String category;

    private Integer calories;

    private BigDecimal protein;

    private BigDecimal carbs;

    private BigDecimal fat;

    private BigDecimal fiber;

    private String unit;
}
