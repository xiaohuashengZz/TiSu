package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.dao.mapper.*;
import com.tisu.model.entity.*;
import com.tisu.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserMapper userMapper;
    private final DishMapper dishMapper;
    private final FoodMapper foodMapper;
    private final ExerciseMapper exerciseMapper;
    private final OperationLogMapper operationLogMapper;

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new LinkedHashMap<>();

        // Count users
        Long userCount = userMapper.selectCount(new LambdaQueryWrapper<>());
        stats.put("userCount", userCount);

        // Count dishes
        Long dishCount = dishMapper.selectCount(new LambdaQueryWrapper<>());
        stats.put("dishCount", dishCount);

        // Count foods
        Long foodCount = foodMapper.selectCount(new LambdaQueryWrapper<>());
        stats.put("foodCount", foodCount);

        // Count exercises
        Long exerciseCount = exerciseMapper.selectCount(new LambdaQueryWrapper<>());
        stats.put("exerciseCount", exerciseCount);

        return stats;
    }

    @Override
    public List<Map<String, Object>> getActivities(int limit) {
        LambdaQueryWrapper<OperationLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(OperationLogEntity::getCreatedAt)
                .last("LIMIT " + limit);
        List<OperationLogEntity> logs = operationLogMapper.selectList(wrapper);

        return logs.stream().map(log -> {
            Map<String, Object> activity = new LinkedHashMap<>();
            activity.put("id", log.getId());
            activity.put("adminId", log.getAdminId());
            activity.put("module", log.getModule());
            activity.put("action", log.getAction());
            activity.put("targetId", log.getTargetId());
            activity.put("targetName", log.getTargetName());
            activity.put("detail", log.getDetail());
            activity.put("createdAt", log.getCreatedAt());
            return activity;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> getOverview() {
        Map<String, Object> overview = new LinkedHashMap<>();

        // Official vs user counts
        LambdaQueryWrapper<FoodEntity> officialFoodWrapper = new LambdaQueryWrapper<>();
        officialFoodWrapper.eq(FoodEntity::getSource, "official");
        Long officialFoodCount = foodMapper.selectCount(officialFoodWrapper);

        LambdaQueryWrapper<FoodEntity> userFoodWrapper = new LambdaQueryWrapper<>();
        userFoodWrapper.eq(FoodEntity::getSource, "user");
        Long userFoodCount = foodMapper.selectCount(userFoodWrapper);

        LambdaQueryWrapper<DishEntity> officialDishWrapper = new LambdaQueryWrapper<>();
        officialDishWrapper.eq(DishEntity::getSource, "official");
        Long officialDishCount = dishMapper.selectCount(officialDishWrapper);

        LambdaQueryWrapper<DishEntity> userDishWrapper = new LambdaQueryWrapper<>();
        userDishWrapper.eq(DishEntity::getSource, "user");
        Long userDishCount = dishMapper.selectCount(userDishWrapper);

        Map<String, Object> sourceStats = new LinkedHashMap<>();
        sourceStats.put("officialFoods", officialFoodCount);
        sourceStats.put("userFoods", userFoodCount);
        sourceStats.put("officialDishes", officialDishCount);
        sourceStats.put("userDishes", userDishCount);
        overview.put("sourceStats", sourceStats);

        // Category stats for foods
        List<FoodEntity> allFoods = foodMapper.selectList(new LambdaQueryWrapper<>());
        Map<String, Long> foodCategoryStats = allFoods.stream()
                .filter(f -> f.getCategory() != null)
                .collect(Collectors.groupingBy(FoodEntity::getCategory, Collectors.counting()));
        overview.put("foodCategoryStats", foodCategoryStats);

        // Category stats for dishes
        List<DishEntity> allDishes = dishMapper.selectList(new LambdaQueryWrapper<>());
        Map<String, Long> dishCategoryStats = allDishes.stream()
                .filter(d -> d.getCategory() != null)
                .collect(Collectors.groupingBy(DishEntity::getCategory, Collectors.counting()));
        overview.put("dishCategoryStats", dishCategoryStats);

        return overview;
    }
}
