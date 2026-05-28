package com.tisu.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AdminDashboardVO {

    private Long userCount;

    private Long dishCount;

    private Long foodCount;

    private Long exerciseCount;

    private List<Object> activities;

    private Map<String, Object> overview;
}
