package com.tisu.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("ai_report")
public class AiReportEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String title;
    private String type;
    private LocalDate periodStart;
    private LocalDate periodEnd;
    private String summary;
    private String content;
    private LocalDateTime createdAt;
}
