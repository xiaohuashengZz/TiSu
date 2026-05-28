package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("operation_log")
public class OperationLogEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long adminId;
    private String module;
    private String action;
    private Long targetId;
    private String targetName;
    private String detail;
    private String ip;
    private LocalDateTime createdAt;
}
