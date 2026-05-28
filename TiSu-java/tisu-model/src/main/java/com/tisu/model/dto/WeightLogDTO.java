package com.tisu.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WeightLogDTO {

    private String date;

    private BigDecimal weight;

    private BigDecimal bodyFat;
}
