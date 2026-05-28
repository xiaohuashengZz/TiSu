package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("weight_log")
public class WeightLogEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate date;
    private BigDecimal weight;
    private BigDecimal bodyFat;
    private BigDecimal bmi;
    private LocalDateTime createdAt;
}
