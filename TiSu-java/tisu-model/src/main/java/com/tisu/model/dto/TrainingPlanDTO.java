package com.tisu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainingPlanDTO {

    private String name;

    private String description;

    private Integer durationDays;

    private String level;

    private List<TrainingDayDTO> days;
}
