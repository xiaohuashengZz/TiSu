package com.tisu.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class FoodVO {

    private Long id;

    private String name;

    private String category;

    private Integer calories;

    private BigDecimal protein;

    private BigDecimal carbs;

    private BigDecimal fat;

    private BigDecimal fiber;

    private String unit;

    private String source;
}
