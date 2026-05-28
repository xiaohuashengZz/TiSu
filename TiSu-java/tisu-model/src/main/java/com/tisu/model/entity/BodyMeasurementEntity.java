package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("body_measurement")
public class BodyMeasurementEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate date;
    private BigDecimal chest;
    private BigDecimal waist;
    private BigDecimal hip;
    private BigDecimal arm;
    private BigDecimal thigh;
    private LocalDateTime createdAt;
}
