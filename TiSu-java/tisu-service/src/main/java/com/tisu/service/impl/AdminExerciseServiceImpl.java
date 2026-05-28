package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.result.PageResult;
import com.tisu.dao.mapper.ExerciseMapper;
import com.tisu.model.dto.ExerciseDTO;
import com.tisu.model.entity.ExerciseEntity;
import com.tisu.model.vo.ExerciseVO;
import com.tisu.service.AdminExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminExerciseServiceImpl implements AdminExerciseService {

    private final ExerciseMapper exerciseMapper;

    @Override
    public PageResult<ExerciseVO> listExercises(String muscleGroup, String keyword, int page, int size) {
        Page<ExerciseEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<ExerciseEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(muscleGroup)) {
            wrapper.eq(ExerciseEntity::getMuscleGroup, muscleGroup);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(ExerciseEntity::getName, keyword);
        }
        wrapper.orderByDesc(ExerciseEntity::getCreatedAt);

        Page<ExerciseEntity> result = exerciseMapper.selectPage(pageParam, wrapper);
        List<ExerciseVO> records = result.getRecords().stream().map(this::toExerciseVO).collect(Collectors.toList());
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    @Transactional
    public void createExercise(ExerciseDTO dto) {
        ExerciseEntity exercise = new ExerciseEntity();
        exercise.setName(dto.getName());
        exercise.setMuscleGroup(dto.getMuscleGroup());
        exercise.setDifficulty(dto.getDifficulty());
        exercise.setDescription(dto.getDescription());
        exercise.setSets(dto.getSets());
        exercise.setReps(dto.getReps());
        exercise.setCreatedAt(LocalDateTime.now());
        exercise.setUpdatedAt(LocalDateTime.now());
        exerciseMapper.insert(exercise);
    }

    @Override
    @Transactional
    public void updateExercise(Long id, ExerciseDTO dto) {
        ExerciseEntity exercise = exerciseMapper.selectById(id);
        if (exercise == null) {
            throw new BusinessException(404, "运动不存在");
        }

        exercise.setName(dto.getName());
        exercise.setMuscleGroup(dto.getMuscleGroup());
        exercise.setDifficulty(dto.getDifficulty());
        exercise.setDescription(dto.getDescription());
        exercise.setSets(dto.getSets());
        exercise.setReps(dto.getReps());
        exercise.setUpdatedAt(LocalDateTime.now());
        exerciseMapper.updateById(exercise);
    }

    @Override
    @Transactional
    public void deleteExercise(Long id) {
        ExerciseEntity exercise = exerciseMapper.selectById(id);
        if (exercise == null) {
            throw new BusinessException(404, "运动不存在");
        }
        exerciseMapper.deleteById(id);
    }

    private ExerciseVO toExerciseVO(ExerciseEntity entity) {
        ExerciseVO vo = new ExerciseVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setMuscleGroup(entity.getMuscleGroup());
        vo.setDifficulty(entity.getDifficulty());
        vo.setDescription(entity.getDescription());
        vo.setSets(entity.getSets());
        vo.setReps(entity.getReps());
        return vo;
    }
}
