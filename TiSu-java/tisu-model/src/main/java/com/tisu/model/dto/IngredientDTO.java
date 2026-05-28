package com.tisu.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class IngredientDTO {

    private Long foodId;

    private BigDecimal amount;

    private String unit;
}
