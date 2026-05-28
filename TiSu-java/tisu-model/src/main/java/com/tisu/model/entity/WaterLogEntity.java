package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("water_log")
public class WaterLogEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate date;
    private Integer amountMl;
    private LocalDateTime createdAt;
}
