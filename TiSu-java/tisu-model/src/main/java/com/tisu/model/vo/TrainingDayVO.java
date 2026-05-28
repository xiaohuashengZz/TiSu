package com.tisu.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class TrainingDayVO {

    private Integer dayNumber;

    private String label;

    private Integer isRestDay;

    private List<TrainingExerciseVO> exercises;
}
