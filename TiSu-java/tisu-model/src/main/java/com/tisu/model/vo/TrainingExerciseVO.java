package com.tisu.model.vo;

import lombok.Data;

@Data
public class TrainingExerciseVO {

    private Long exerciseId;

    private String exerciseName;

    private Integer sets;

    private Integer reps;

    private String weight;
}
