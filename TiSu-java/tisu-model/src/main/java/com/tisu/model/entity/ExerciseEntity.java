package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("exercise")
public class ExerciseEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String muscleGroup;
    private String difficulty;
    private String description;
    private Integer sets;
    private Integer reps;
    private String image;
    private String videoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
