package com.tisu.model.vo;

import lombok.Data;

@Data
public class ExerciseVO {

    private Long id;

    private String name;

    private String muscleGroup;

    private String difficulty;

    private String description;

    private Integer sets;

    private Integer reps;
}
