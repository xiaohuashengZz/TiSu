package com.tisu.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientVO {

    private Long foodId;

    private String foodName;

    private BigDecimal amount;

    private String unit;
}
