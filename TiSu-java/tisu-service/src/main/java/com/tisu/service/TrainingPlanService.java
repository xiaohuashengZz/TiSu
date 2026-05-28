package com.tisu.service;

import com.tisu.model.dto.TrainingPlanDTO;
import com.tisu.model.vo.TrainingPlanVO;

import java.util.List;

public interface TrainingPlanService {

    List<TrainingPlanVO> listPlans(Long userId);

    TrainingPlanVO getPlanDetail(Long planId);

    void createPlan(Long userId, TrainingPlanDTO dto);

    void deletePlan(Long userId, Long planId);
}
