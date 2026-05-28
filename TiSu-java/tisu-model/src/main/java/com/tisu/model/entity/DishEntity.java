package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("dish")
public class DishEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String description;
    private Integer calories;
    private String category;
    private String source;
    private Long userId;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
