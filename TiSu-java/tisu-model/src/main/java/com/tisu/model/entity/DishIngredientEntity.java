package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("dish_ingredient")
public class DishIngredientEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long dishId;
    private Long foodId;
    private BigDecimal amount;
    private String unit;
    private LocalDateTime createdAt;
}
