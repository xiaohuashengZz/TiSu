package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("meal_plan")
public class MealPlanEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private LocalDate date;
    private String mealType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
