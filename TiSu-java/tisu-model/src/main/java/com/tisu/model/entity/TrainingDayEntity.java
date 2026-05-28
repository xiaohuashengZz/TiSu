package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("training_day")
public class TrainingDayEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long planId;
    private Integer dayNumber;
    private String label;
    private Integer isRestDay;
    private LocalDateTime createdAt;
}
