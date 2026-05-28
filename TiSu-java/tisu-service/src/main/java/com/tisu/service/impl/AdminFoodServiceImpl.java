package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.result.PageResult;
import com.tisu.dao.mapper.FoodMapper;
import com.tisu.model.dto.FoodDTO;
import com.tisu.model.entity.FoodEntity;
import com.tisu.model.vo.FoodVO;
import com.tisu.service.AdminFoodService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminFoodServiceImpl implements AdminFoodService {

    private final FoodMapper foodMapper;

    @Override
    public PageResult<FoodVO> listFoods(String source, String category, String keyword, int page, int size) {
        Page<FoodEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<FoodEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(source)) {
            wrapper.eq(FoodEntity::getSource, source);
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
    public void createFood(FoodDTO dto) {
        FoodEntity food = new FoodEntity();
        food.setName(dto.getName());
        food.setCategory(dto.getCategory());
        food.setCalories(dto.getCalories());
        food.setProtein(dto.getProtein());
        food.setCarbs(dto.getCarbs());
        food.setFat(dto.getFat());
        food.setFiber(dto.getFiber());
        food.setUnit(dto.getUnit());
        food.setSource("official");
        food.setCreatedAt(LocalDateTime.now());
        food.setUpdatedAt(LocalDateTime.now());
        foodMapper.insert(food);
    }

    @Override
    @Transactional
    public void updateFood(Long id, FoodDTO dto) {
        FoodEntity food = foodMapper.selectById(id);
        if (food == null) {
            throw new BusinessException(404, "食物不存在");
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
    public void deleteFood(Long id) {
        FoodEntity food = foodMapper.selectById(id);
        if (food == null) {
            throw new BusinessException(404, "食物不存在");
        }
        foodMapper.deleteById(id);
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
