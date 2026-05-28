package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("meal_plan_dish")
public class MealPlanDishEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long mealPlanId;
    private Long dishId;
    private LocalDateTime createdAt;
}
