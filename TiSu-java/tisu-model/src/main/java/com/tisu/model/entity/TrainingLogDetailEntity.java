package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("training_log_detail")
public class TrainingLogDetailEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long logId;
    private Long exerciseId;
    private Integer setNumber;
    private Integer reps;
    private BigDecimal weight;
    private Integer completed;
    private LocalDateTime createdAt;
}
