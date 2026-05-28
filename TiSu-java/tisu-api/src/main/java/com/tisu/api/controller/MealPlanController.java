package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.MealPlanDTO;
import com.tisu.model.vo.WeekPlanVO;
import com.tisu.service.MealPlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @GetMapping
    public Result<List<Map<String, Object>>> getDayPlan(HttpServletRequest request,
                                                        @RequestParam String date) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(mealPlanService.getDayPlan(userId, date));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody MealPlanDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        mealPlanService.createMealPlan(userId, dto);
        return Result.success();
    }

    @GetMapping("/week")
    public Result<List<WeekPlanVO>> getWeekPlan(HttpServletRequest request,
                                                @RequestParam String startDate) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(mealPlanService.getWeekPlan(userId, startDate));
    }

    @GetMapping("/month")
    public Result<List<Map<String, Object>>> getMonthPlan(HttpServletRequest request,
                                                          @RequestParam Integer year,
                                                          @RequestParam Integer month) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(mealPlanService.getMonthPlan(userId, year, month));
    }
}
