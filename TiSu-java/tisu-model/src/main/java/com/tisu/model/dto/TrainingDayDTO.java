package com.tisu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainingDayDTO {

    private Integer dayNumber;

    private String label;

    private Integer isRestDay;

    private List<TrainingExerciseDTO> exercises;
}
