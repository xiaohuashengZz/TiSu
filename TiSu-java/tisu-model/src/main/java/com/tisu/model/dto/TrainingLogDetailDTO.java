package com.tisu.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TrainingLogDetailDTO {

    private Long exerciseId;

    private Integer setNumber;

    private Integer reps;

    private BigDecimal weight;

    private Integer completed;
}
