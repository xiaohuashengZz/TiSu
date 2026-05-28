package com.tisu.model.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DashboardVO {

    private Integer todayCalories;

    private Integer todayTarget;

    private Integer todayBurned;

    private BigDecimal weekWeightChange;

    private Integer monthTrainingDays;

    private Integer totalTrainingMinutes;

    private List<Object> weightLogs;

    private List<Integer> weeklyCalories;
}
