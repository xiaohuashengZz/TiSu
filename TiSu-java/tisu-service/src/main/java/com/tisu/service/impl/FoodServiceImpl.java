package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.result.PageResult;
import com.tisu.dao.mapper.FoodMapper;
import com.tisu.model.dto.FoodDTO;
import com.tisu.model.entity.FoodEntity;
import com.tisu.model.vo.FoodVO;
import com.tisu.service.FoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FoodServiceImpl implements FoodService {

    private final FoodMapper foodMapper;

    @Override
    public PageResult<FoodVO> listFoods(Long userId, String source, String category, String keyword, int page, int size) {
        Page<FoodEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<FoodEntity> wrapper = new LambdaQueryWrapper<>();

        // Filter by source: official or user's own
        if (StringUtils.hasText(source)) {
            wrapper.eq(FoodEntity::getSource, source);
            if ("user".equals(source) && userId != null) {
                wrapper.eq(FoodEntity::getUserId, userId);
            }
        } else {
            // Default: show official + user's own
            if (userId != null) {
                wrapper.and(w -> w.eq(FoodEntity::getSource, "official")
                        .or()
                        .and(w2 -> w2.eq(FoodEntity::getSource, "user").eq(FoodEntity::getUserId, userId)));
            } else {
                wrapper.eq(FoodEntity::getSource, "official");
            }
        }

        if (StringUtils.hasText(category)) {
            wrapper.eq(FoodEntity::getCategory, category);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.like(FoodEntity::getName, keyword);
        }
        wrapper.orderByDesc(FoodEntity::getCreatedAt);

        Page<FoodEntity> result = foodMapper.selectPage(pageParam, wrapper);
        List<FoodVO> records = result.getRecords().stream().map(this::toFoodVO).collect(Collectors.toList());
        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    @Transactional
    public void createFood(Long userId, FoodDTO dto) {
        FoodEntity food = new FoodEntity();
        food.setName(dto.getName());
        food.setCategory(dto.getCategory());
        food.setCalories(dto.getCalories());
        food.setProtein(dto.getProtein());
        food.setCarbs(dto.getCarbs());
        food.setFat(dto.getFat());
        food.setFiber(dto.getFiber());
        food.setUnit(dto.getUnit());
        food.setSource("user");
        food.setUserId(userId);
        food.setCreatedAt(LocalDateTime.now());
        food.setUpdatedAt(LocalDateTime.now());
        foodMapper.insert(food);
    }

    @Override
    @Transactional
    public void updateFood(Long userId, Long foodId, FoodDTO dto) {
        FoodEntity food = foodMapper.selectById(foodId);
        if (food == null) {
            throw new BusinessException(404, "食物不存在");
        }
        if (!"user".equals(food.getSource()) || !userId.equals(food.getUserId())) {
            throw new BusinessException(403, "无权修改该食物");
        }

        food.setName(dto.getName());
        food.setCategory(dto.getCategory());
        food.setCalories(dto.getCalories());
        food.setProtein(dto.getProtein());
        food.setCarbs(dto.getCarbs());
        food.setFat(dto.getFat());
        food.setFiber(dto.getFiber());
        food.setUnit(dto.getUnit());
        food.setUpdatedAt(LocalDateTime.now());
        foodMapper.updateById(food);
    }

    @Override
    @Transactional
    public void deleteFood(Long userId, Long foodId) {
        FoodEntity food = foodMapper.selectById(foodId);
        if (food == null) {
            throw new BusinessException(404, "食物不存在");
        }
        if (!"user".equals(food.getSource()) || !userId.equals(food.getUserId())) {
            throw new BusinessException(403, "无权删除该食物");
        }
        foodMapper.deleteById(foodId);
    }

    private FoodVO toFoodVO(FoodEntity entity) {
        FoodVO vo = new FoodVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setCategory(entity.getCategory());
        vo.setCalories(entity.getCalories());
        vo.setProtein(entity.getProtein());
        vo.setCarbs(entity.getCarbs());
        vo.setFat(entity.getFat());
        vo.setFiber(entity.getFiber());
        vo.setUnit(entity.getUnit());
        vo.setSource(entity.getSource());
        return vo;
    }
}
