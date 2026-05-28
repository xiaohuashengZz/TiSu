package com.tisu.model.dto;

import lombok.Data;

@Data
public class ExerciseDTO {

    private String name;

    private String muscleGroup;

    private String difficulty;

    private String description;

    private Integer sets;

    private Integer reps;
}
