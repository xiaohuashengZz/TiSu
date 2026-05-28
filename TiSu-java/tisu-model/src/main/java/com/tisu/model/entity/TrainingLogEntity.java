package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("training_log")
public class TrainingLogEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Long planId;
    private LocalDate date;
    private Integer duration;
    private String notes;
    private Integer caloriesBurned;
    private LocalDateTime createdAt;
}
