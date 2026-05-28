package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("training_exercise")
public class TrainingExerciseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dayId;
    private Long exerciseId;
    private Integer sets;
    private Integer reps;
    private String weight;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
