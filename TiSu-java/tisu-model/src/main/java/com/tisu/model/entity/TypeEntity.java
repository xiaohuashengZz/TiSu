package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("type")
public class TypeEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String typeGroup;
    private String typeValue;
    private Integer sortOrder;
    private LocalDateTime createdAt;
}
