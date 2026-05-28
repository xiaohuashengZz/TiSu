package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.dao.mapper.*;
import com.tisu.model.dto.TypeDTO;
import com.tisu.model.entity.*;
import com.tisu.service.AdminTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTypeServiceImpl implements AdminTypeService {

    private final TypeMapper typeMapper;
    private final FoodMapper foodMapper;
    private final DishMapper dishMapper;
    private final ExerciseMapper exerciseMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public List<TypeEntity> listTypes(String typeGroup) {
        LambdaQueryWrapper<TypeEntity> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(typeGroup)) {
            wrapper.eq(TypeEntity::getTypeGroup, typeGroup);
        }
        wrapper.orderByAsc(TypeEntity::getSortOrder);
        return typeMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void createType(TypeDTO dto) {
        // Check uniqueness: typeGroup + typeValue (typeName maps to typeValue in entity)
        LambdaQueryWrapper<TypeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TypeEntity::getTypeGroup, dto.getTypeGroup())
                .eq(TypeEntity::getTypeValue, dto.getTypeName());
        Long count = typeMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "该类型已存在");
        }

        TypeEntity type = new TypeEntity();
        type.setTypeGroup(dto.getTypeGroup());
        type.setTypeValue(dto.getTypeName());
        type.setSortOrder(dto.getSortOrder());
        type.setCreatedAt(LocalDateTime.now());
        typeMapper.insert(type);
    }

    @Override
    @Transactional
    public void updateType(Long id, TypeDTO dto) {
        TypeEntity type = typeMapper.selectById(id);
        if (type == null) {
            throw new BusinessException(404, "类型不存在");
        }

        // Check uniqueness if group or value changed
        if (!type.getTypeGroup().equals(dto.getTypeGroup()) || !type.getTypeValue().equals(dto.getTypeName())) {
            LambdaQueryWrapper<TypeEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TypeEntity::getTypeGroup, dto.getTypeGroup())
                    .eq(TypeEntity::getTypeValue, dto.getTypeName())
                    .ne(TypeEntity::getId, id);
            Long count = typeMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException(400, "该类型已存在");
            }
        }

        type.setTypeGroup(dto.getTypeGroup());
        type.setTypeValue(dto.getTypeName());
        type.setSortOrder(dto.getSortOrder());
        typeMapper.updateById(type);
    }

    @Override
    @Transactional
    public void deleteType(Long id) {
        TypeEntity type = typeMapper.selectById(id);
        if (type == null) {
            throw new BusinessException(404, "类型不存在");
        }

        // Check references based on typeGroup
        long refCount = 0;
        String group = type.getTypeGroup();
        String value = type.getTypeValue();

        switch (group) {
            case "food_category":
                refCount = foodMapper.selectCount(
                        new LambdaQueryWrapper<FoodEntity>().eq(FoodEntity::getCategory, value));
                break;
            case "dish_category":
                refCount = dishMapper.selectCount(
                        new LambdaQueryWrapper<DishEntity>().eq(DishEntity::getCategory, value));
                break;
            case "muscle_group":
                refCount = exerciseMapper.selectCount(
                        new LambdaQueryWrapper<ExerciseEntity>().eq(ExerciseEntity::getMuscleGroup, value));
                break;
            case "difficulty":
                refCount = exerciseMapper.selectCount(
                        new LambdaQueryWrapper<ExerciseEntity>().eq(ExerciseEntity::getDifficulty, value));
                break;
            case "fitness_goal":
                refCount = userProfileMapper.selectCount(
                        new LambdaQueryWrapper<UserProfileEntity>().eq(UserProfileEntity::getFitnessGoal, value));
                break;
            case "activity_level":
                refCount = userProfileMapper.selectCount(
                        new LambdaQueryWrapper<UserProfileEntity>().eq(UserProfileEntity::getActivityLevel, value));
                break;
            default:
                break;
        }

        if (refCount > 0) {
            throw new BusinessException(409, "该分类正在被 " + refCount + " 条数据引用，无法删除");
        }

        typeMapper.deleteById(id);
    }
}
