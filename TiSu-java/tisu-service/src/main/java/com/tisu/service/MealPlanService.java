package com.tisu.service;

import com.tisu.model.dto.MealPlanDTO;
import com.tisu.model.vo.WeekPlanVO;

import java.util.List;
import java.util.Map;

public interface MealPlanService {

    List<WeekPlanVO> getWeekPlan(Long userId, String startDate);

    List<Map<String, Object>> getMonthPlan(Long userId, int year, int month);

    void createMealPlan(Long userId, MealPlanDTO dto);

    List<Map<String, Object>> getDayPlan(Long userId, String date);
}
