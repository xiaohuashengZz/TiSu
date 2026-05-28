package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.dao.mapper.ExerciseMapper;
import com.tisu.dao.mapper.TrainingDayMapper;
import com.tisu.dao.mapper.TrainingExerciseMapper;
import com.tisu.dao.mapper.TrainingPlanMapper;
import com.tisu.model.dto.TrainingDayDTO;
import com.tisu.model.dto.TrainingExerciseDTO;
import com.tisu.model.dto.TrainingPlanDTO;
import com.tisu.model.entity.ExerciseEntity;
import com.tisu.model.entity.TrainingDayEntity;
import com.tisu.model.entity.TrainingExerciseEntity;
import com.tisu.model.entity.TrainingPlanEntity;
import com.tisu.model.vo.TrainingDayVO;
import com.tisu.model.vo.TrainingExerciseVO;
import com.tisu.model.vo.TrainingPlanVO;
import com.tisu.service.TrainingPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final TrainingPlanMapper trainingPlanMapper;
    private final TrainingDayMapper trainingDayMapper;
    private final TrainingExerciseMapper trainingExerciseMapper;
    private final ExerciseMapper exerciseMapper;

    @Override
    public List<TrainingPlanVO> listPlans(Long userId) {
        LambdaQueryWrapper<TrainingPlanEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainingPlanEntity::getUserId, userId)
                .orderByDesc(TrainingPlanEntity::getCreatedAt);
        List<TrainingPlanEntity> plans = trainingPlanMapper.selectList(wrapper);

        return plans.stream().map(plan -> {
            TrainingPlanVO vo = new TrainingPlanVO();
            vo.setId(plan.getId());
            vo.setName(plan.getName());
            vo.setDescription(plan.getDescription());
            vo.setDurationDays(plan.getDurationDays());
            vo.setLevel(plan.getLevel());
            vo.setDays(getDays(plan.getId()));
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public TrainingPlanVO getPlanDetail(Long planId) {
        TrainingPlanEntity plan = trainingPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException(404, "训练计划不存在");
        }

        TrainingPlanVO vo = new TrainingPlanVO();
        vo.setId(plan.getId());
        vo.setName(plan.getName());
        vo.setDescription(plan.getDescription());
        vo.setDurationDays(plan.getDurationDays());
        vo.setLevel(plan.getLevel());
        vo.setDays(getDays(planId));
        return vo;
    }

    @Override
    @Transactional
    public void createPlan(Long userId, TrainingPlanDTO dto) {
        TrainingPlanEntity plan = new TrainingPlanEntity();
        plan.setUserId(userId);
        plan.setName(dto.getName());
        plan.setDescription(dto.getDescription());
        plan.setDurationDays(dto.getDurationDays());
        plan.setLevel(dto.getLevel());
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        trainingPlanMapper.insert(plan);

        if (dto.getDays() != null) {
            for (TrainingDayDTO dayDTO : dto.getDays()) {
                TrainingDayEntity day = new TrainingDayEntity();
                day.setPlanId(plan.getId());
                day.setDayNumber(dayDTO.getDayNumber());
                day.setLabel(dayDTO.getLabel());
                day.setIsRestDay(dayDTO.getIsRestDay());
                day.setCreatedAt(LocalDateTime.now());
                trainingDayMapper.insert(day);

                if (dayDTO.getExercises() != null && dayDTO.getIsRestDay() != 1) {
                    for (TrainingExerciseDTO exDTO : dayDTO.getExercises()) {
                        TrainingExerciseEntity exercise = new TrainingExerciseEntity();
                        exercise.setDayId(day.getId());
                        exercise.setExerciseId(exDTO.getExerciseId());
                        exercise.setSets(exDTO.getSets());
                        exercise.setReps(exDTO.getReps());
                        exercise.setWeight(exDTO.getWeight());
                        exercise.setCreatedAt(LocalDateTime.now());
                        trainingExerciseMapper.insert(exercise);
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public void deletePlan(Long userId, Long planId) {
        TrainingPlanEntity plan = trainingPlanMapper.selectById(planId);
        if (plan == null) {
            throw new BusinessException(404, "训练计划不存在");
        }
        if (!userId.equals(plan.getUserId())) {
            throw new BusinessException(403, "无权删除该训练计划");
        }

        // Delete exercises -> days -> plan
        LambdaQueryWrapper<TrainingDayEntity> dayWrapper = new LambdaQueryWrapper<>();
        dayWrapper.eq(TrainingDayEntity::getPlanId, planId);
        List<TrainingDayEntity> days = trainingDayMapper.selectList(dayWrapper);

        for (TrainingDayEntity day : days) {
            LambdaQueryWrapper<TrainingExerciseEntity> exWrapper = new LambdaQueryWrapper<>();
            exWrapper.eq(TrainingExerciseEntity::getDayId, day.getId());
            trainingExerciseMapper.delete(exWrapper);
        }

        trainingDayMapper.delete(dayWrapper);
        trainingPlanMapper.deleteById(planId);
    }

    private List<TrainingDayVO> getDays(Long planId) {
        LambdaQueryWrapper<TrainingDayEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainingDayEntity::getPlanId, planId)
                .orderByAsc(TrainingDayEntity::getDayNumber);
        List<TrainingDayEntity> days = trainingDayMapper.selectList(wrapper);

        if (days.isEmpty()) {
            return new ArrayList<>();
        }

        // Get all day IDs
        List<Long> dayIds = days.stream().map(TrainingDayEntity::getId).collect(Collectors.toList());

        // Get all exercises for these days
        LambdaQueryWrapper<TrainingExerciseEntity> exWrapper = new LambdaQueryWrapper<>();
        exWrapper.in(TrainingExerciseEntity::getDayId, dayIds);
        List<TrainingExerciseEntity> allExercises = trainingExerciseMapper.selectList(exWrapper);

        // Get all exercise names
        List<Long> exerciseIds = allExercises.stream()
                .map(TrainingExerciseEntity::getExerciseId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, String> exerciseNameMap = new java.util.HashMap<>();
        if (!exerciseIds.isEmpty()) {
            List<ExerciseEntity> exercises = exerciseMapper.selectBatchIds(exerciseIds);
            exerciseNameMap = exercises.stream()
                    .collect(Collectors.toMap(ExerciseEntity::getId, ExerciseEntity::getName));
        }

        Map<Long, String> finalExerciseNameMap = exerciseNameMap;
        Map<Long, List<TrainingExerciseEntity>> dayExerciseMap = allExercises.stream()
                .collect(Collectors.groupingBy(TrainingExerciseEntity::getDayId));

        return days.stream().map(day -> {
            TrainingDayVO dayVO = new TrainingDayVO();
            dayVO.setDayNumber(day.getDayNumber());
            dayVO.setLabel(day.getLabel());
            dayVO.setIsRestDay(day.getIsRestDay());

            List<TrainingExerciseEntity> exercises = dayExerciseMap.getOrDefault(day.getId(), new ArrayList<>());
            List<TrainingExerciseVO> exerciseVOs = exercises.stream().map(ex -> {
                TrainingExerciseVO evo = new TrainingExerciseVO();
                evo.setExerciseId(ex.getExerciseId());
                evo.setExerciseName(finalExerciseNameMap.getOrDefault(ex.getExerciseId(), ""));
                evo.setSets(ex.getSets());
                evo.setReps(ex.getReps());
                evo.setWeight(ex.getWeight());
                return evo;
            }).collect(Collectors.toList());

            dayVO.setExercises(exerciseVOs);
            return dayVO;
        }).collect(Collectors.toList());
    }
}
