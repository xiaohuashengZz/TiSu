package com.tisu.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WeightLogVO {

    private Long id;

    private LocalDate date;

    private BigDecimal weight;

    private BigDecimal bodyFat;

    private BigDecimal bmi;
}
