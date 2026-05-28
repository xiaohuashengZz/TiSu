package com.tisu.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingPlanVO {

    private Long id;

    private String name;

    private String description;

    private Integer durationDays;

    private String level;

    private List<TrainingDayVO> days;
}
