package com.tisu.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainingLogDTO {

    private Long planId;

    private String date;

    private Integer duration;

    private String notes;

    private Integer caloriesBurned;

    private List<TrainingLogDetailDTO> details;
}
