package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.TrainingPlanDTO;
import com.tisu.model.vo.TrainingPlanVO;
import com.tisu.service.TrainingPlanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/training-plans")
@RequiredArgsConstructor
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;

    @GetMapping
    public Result<List<TrainingPlanVO>> list(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(trainingPlanService.listPlans(userId));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody TrainingPlanDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        trainingPlanService.createPlan(userId, dto);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<TrainingPlanVO> detail(@PathVariable Long id) {
        return Result.success(trainingPlanService.getPlanDetail(id));
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(HttpServletRequest request, @PathVariable Long id) {
        Long userId = (Long) request.getAttribute("userId");
        trainingPlanService.deletePlan(userId, id);
        return Result.success();
    }
}
