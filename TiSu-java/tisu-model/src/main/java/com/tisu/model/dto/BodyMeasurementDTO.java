package com.tisu.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BodyMeasurementDTO {

    private String date;

    private BigDecimal chest;

    private BigDecimal waist;

    private BigDecimal hip;

    private BigDecimal arm;

    private BigDecimal thigh;
}
