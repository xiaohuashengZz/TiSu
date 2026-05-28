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
import com.tisu.service.DishService;
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
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;
    private final DishIngredientMapper dishIngredientMapper;
    private final FoodMapper foodMapper;

    @Override
    public PageResult<DishVO> listDishes(Long userId, String source, String category, String keyword, int page, int size) {
        Page<DishEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<DishEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(source)) {
            wrapper.eq(DishEntity::getSource, source);
            if ("user".equals(source) && userId != null) {
                wrapper.eq(DishEntity::getUserId, userId);
            }
        } else {
            if (userId != null) {
                wrapper.and(w -> w.eq(DishEntity::getSource, "official")
                        .or()
                        .and(w2 -> w2.eq(DishEntity::getSource, "user").eq(DishEntity::getUserId, userId)));
            } else {
                wrapper.eq(DishEntity::getSource, "official");
            }
        }

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
    public DishVO getDishDetail(Long dishId) {
        DishEntity dish = dishMapper.selectById(dishId);
        if (dish == null) {
            throw new BusinessException(404, "菜品不存在");
        }
        DishVO vo = toDishVO(dish);
        vo.setIngredients(getIngredients(dishId));
        return vo;
    }

    @Override
    @Transactional
    public void createDish(Long userId, DishDTO dto) {
        // Calculate total calories from ingredients
        int totalCalories = calculateCalories(dto.getIngredients());

        DishEntity dish = new DishEntity();
        dish.setName(dto.getName());
        dish.setDescription(dto.getDescription());
        dish.setCategory(dto.getCategory());
        dish.setCalories(totalCalories);
        dish.setSource("user");
        dish.setUserId(userId);
        dish.setCreatedAt(LocalDateTime.now());
        dish.setUpdatedAt(LocalDateTime.now());
        dishMapper.insert(dish);

        // Insert ingredients
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
    public void updateDish(Long userId, Long dishId, DishDTO dto) {
        DishEntity dish = dishMapper.selectById(dishId);
        if (dish == null) {
            throw new BusinessException(404, "菜品不存在");
        }
        if (!"user".equals(dish.getSource()) || !userId.equals(dish.getUserId())) {
            throw new BusinessException(403, "无权修改该菜品");
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
        wrapper.eq(DishIngredientEntity::getDishId, dishId);
        dishIngredientMapper.delete(wrapper);

        if (dto.getIngredients() != null) {
            for (IngredientDTO ingredient : dto.getIngredients()) {
                DishIngredientEntity entity = new DishIngredientEntity();
                entity.setDishId(dishId);
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
    public void deleteDish(Long userId, Long dishId) {
        DishEntity dish = dishMapper.selectById(dishId);
        if (dish == null) {
            throw new BusinessException(404, "菜品不存在");
        }
        if (!"user".equals(dish.getSource()) || !userId.equals(dish.getUserId())) {
            throw new BusinessException(403, "无权删除该菜品");
        }

        // Delete ingredients first
        LambdaQueryWrapper<DishIngredientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishIngredientEntity::getDishId, dishId);
        dishIngredientMapper.delete(wrapper);

        dishMapper.deleteById(dishId);
    }

    @Override
    public List<IngredientVO> getIngredients(Long dishId) {
        LambdaQueryWrapper<DishIngredientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishIngredientEntity::getDishId, dishId);
        List<DishIngredientEntity> ingredients = dishIngredientMapper.selectList(wrapper);

        if (ingredients.isEmpty()) {
            return new ArrayList<>();
        }

        // Batch get food names
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

    @Override
    @Transactional
    public void updateIngredients(Long userId, Long dishId, List<IngredientDTO> ingredients) {
        DishEntity dish = dishMapper.selectById(dishId);
        if (dish == null) {
            throw new BusinessException(404, "菜品不存在");
        }
        if (!"user".equals(dish.getSource()) || !userId.equals(dish.getUserId())) {
            throw new BusinessException(403, "无权修改该菜品");
        }

        // Replace ingredients
        LambdaQueryWrapper<DishIngredientEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishIngredientEntity::getDishId, dishId);
        dishIngredientMapper.delete(wrapper);

        if (ingredients != null) {
            for (IngredientDTO ingredient : ingredients) {
                DishIngredientEntity entity = new DishIngredientEntity();
                entity.setDishId(dishId);
                entity.setFoodId(ingredient.getFoodId());
                entity.setAmount(ingredient.getAmount());
                entity.setUnit(ingredient.getUnit());
                entity.setCreatedAt(LocalDateTime.now());
                dishIngredientMapper.insert(entity);
            }
        }

        // Recalculate calories
        int totalCalories = calculateCalories(ingredients);
        dish.setCalories(totalCalories);
        dish.setUpdatedAt(LocalDateTime.now());
        dishMapper.updateById(dish);
    }

    private int calculateCalories(List<IngredientDTO> ingredients) {
        if (ingredients == null || ingredients.isEmpty()) {
            return 0;
        }
        int totalCalories = 0;
        for (IngredientDTO ingredient : ingredients) {
            FoodEntity food = foodMapper.selectById(ingredient.getFoodId());
            if (food != null && food.getCalories() != null) {
                // Calories per unit * amount
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
