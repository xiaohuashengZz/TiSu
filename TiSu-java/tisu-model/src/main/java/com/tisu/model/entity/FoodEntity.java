package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("food")
public class FoodEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String category;
    private Integer calories;
    private BigDecimal protein;
    private BigDecimal carbs;
    private BigDecimal fat;
    private BigDecimal fiber;
    private String unit;
    private String source;
    private Long userId;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
