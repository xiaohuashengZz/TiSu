package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.result.PageResult;
import com.tisu.dao.mapper.DishIngredientMapper;
import com.tisu.dao.mapper.DishMapper;
import com.tisu.dao.mapper.FoodMapper;
import com.tisu.model.dto.DishDTO;
import com.tisu.model.dto.IngredientDTO;
import com.tisu.model.entity.DishEntity;
import com.tisu.model.entity.DishIngredientEntity;
import com.tisu.model.entity.FoodEntity;
import com.tisu.model.vo.DishVO;
import com.tisu.model.vo.IngredientVO;
import com.tisu.service.AdminDishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDishServiceImpl implements AdminDishService {

    private final DishMapper dishMapper;
    private final DishIngredientMapper dishIngredientMapper;
    private final FoodMapper foodMapper;

    @Override
    public PageResult<DishVO> listDishes(String category, String keyword, int page, int size) {
        Page<DishEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<DishEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(category)) {
            wrapper.eq(DishEntity::getCategory, category);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(DishEntity::getName, keyword);
        }
        wrapper.orderByDesc(DishEntity::getCreatedAt);

        Page<DishEntity> result = dishMapper.selectPage(pageParam, wrapper);
        List<DishVO> records = result.getRecords().stream().map(entity -> {
            DishVO vo = toDishVO(entity);
            vo.setIngredients(getIngredients(entity.getId()));
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    @Transactional
    public void createDish(DishDTO dto) {
        int totalCalories = calculateCalories(dto.getIngredients());

        DishEntity dish = new DishEntity();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setCategory(dto.getCategory());
        dish.setCalories(totalCalories);
        dish.setSource("official");
        dish.setCreatedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());
        dishMapper.insert(dish);

        if (dto.getIngredients() != null) {
            for (IngredientDTO ingredient : dto.getIngredients()) {
                DishIngredientEntity entity = new DishIngredientEntity();
                entity.setDishId(dish.getId());
                entity.setFoodId(ingredient.getFoodId());
                entity.setAmount(ingredient.getAmount());
                entity.setUnit(ingredient.getUnit());
                entity.setCreatedAt(LocalDateTime.now());
                dishIngredientMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void updateDish(Long id, DishDTO dto) {
        DishEntity dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BusinessException(404, "菜品不存在");
        }

        int totalCalories = calculateCalories(dto.getIngredients());

        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setCategory(dto.getCategory());
        dish.setCalories(totalCalories);
        dish.setUpdatedAt(LocalDateTime.now());
        dishMapper.updateById(dish);

        // Replace ingredients
        LambdaQueryWrapper<DishIngredientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishIngredientEntity::getDishId, id);
        dishIngredientMapper.delete(wrapper);

        if (dto.getIngredients() != null) {
            for (IngredientDTO ingredient : dto.getIngredients()) {
                DishIngredientEntity entity = new DishIngredientEntity();
                entity.setDishId(id);
                entity.setFoodId(ingredient.getFoodId());
                entity.setAmount(ingredient.getAmount());
                entity.setUnit(ingredient.getUnit());
                entity.setCreatedAt(LocalDateTime.now());
                dishIngredientMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional
    public void deleteDish(Long id) {
        DishEntity dish = dishMapper.selectById(id);
        if (dish == null) {
            throw new BusinessException(404, "菜品不存在");
        }

        // Delete ingredients first
        LambdaQueryWrapper<DishIngredientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishIngredientEntity::getDishId, id);
        dishIngredientMapper.delete(wrapper);

        dishMapper.deleteById(id);
    }

    private List<IngredientVO> getIngredients(Long dishId) {
        LambdaQueryWrapper<DishIngredientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishIngredientEntity::getDishId, dishId);
        List<DishIngredientEntity> ingredients = dishIngredientMapper.selectList(wrapper);

        if (ingredients.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> foodIds = ingredients.stream()
                .map(DishIngredientEntity::getFoodId)
                .distinct()
                .collect(Collectors.toList());
        List<FoodEntity> foods = foodMapper.selectBatchIds(foodIds);
        Map<Long, String> foodNameMap = foods.stream()
                .collect(Collectors.toMap(FoodEntity::getId, FoodEntity::getName));

        return ingredients.stream().map(ing -> {
            IngredientVO vo = new IngredientVO();
            vo.setFoodId(ing.getFoodId());
            vo.setFoodName(foodNameMap.getOrDefault(ing.getFoodId(), ""));
            vo.setAmount(ing.getAmount());
            vo.setUnit(ing.getUnit());
            return vo;
        }).collect(Collectors.toList());
    }

    private int calculateCalories(List<IngredientDTO> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0;
        }
        int totalCalories = 0;
        for (IngredientDTO ingredient : ingredients) {
            FoodEntity food = foodMapper.selectById(ingredient.getFoodId());
            if (food != null && food.getCalories() != null) {
                totalCalories += (int) (food.getCalories() * ingredient.getAmount().doubleValue());
            }
        }
        return totalCalories;
    }

    private DishVO toDishVO(DishEntity entity) {
        DishVO vo = new DishVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setDescription(entity.getDescription());
        vo.setCalories(entity.getCalories());
        vo.setCategory(entity.getCategory());
        vo.setSource(entity.getSource());
        return vo;
    }
}
