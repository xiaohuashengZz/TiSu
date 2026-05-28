package com.tisu.model.dto;

import lombok.Data;

@Data
public class TrainingExerciseDTO {

    private Long exerciseId;

    private Integer sets;

    private Integer reps;

    private String weight;
}
