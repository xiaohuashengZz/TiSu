package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.dao.mapper.*;
import com.tisu.model.dto.MealPlanDTO;
import com.tisu.model.entity.*;
import com.tisu.model.vo.DishSimpleVO;
import com.tisu.model.vo.IngredientVO;
import com.tisu.model.vo.MealVO;
import com.tisu.model.vo.WeekPlanVO;
import com.tisu.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MealPlanServiceImpl implements MealPlanService {

    private final MealPlanMapper mealPlanMapper;
    private final MealPlanDishMapper mealPlanDishMapper;
    private final DishMapper dishMapper;
    private final DishIngredientMapper dishIngredientMapper;
    private final FoodMapper foodMapper;

    @Override
    public List<WeekPlanVO> getWeekPlan(Long userId, String startDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE);
        List<WeekPlanVO> weekPlan = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = start.plusDays(i);
            WeekPlanVO dayVO = new WeekPlanVO();
            dayVO.setDay(currentDate.toString());

            // Get meal plans for this day
            LambdaQueryWrapper<MealPlanEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(MealPlanEntity::getUserId, userId)
                    .eq(MealPlanEntity::getDate, currentDate);
            List<MealPlanEntity> mealPlans = mealPlanMapper.selectList(wrapper);

            List<MealVO> meals = new ArrayList<>();
            int totalCalories = 0;

            for (MealPlanEntity plan : mealPlans) {
                MealVO mealVO = new MealVO();
                mealVO.setType(plan.getMealType());

                // Get dishes for this meal
                LambdaQueryWrapper<MealPlanDishEntity> mpdWrapper = new LambdaQueryWrapper<>();
                mpdWrapper.eq(MealPlanDishEntity::getMealPlanId, plan.getId());
                List<MealPlanDishEntity> planDishes = mealPlanDishMapper.selectList(mpdWrapper);

                List<DishSimpleVO> dishSimpleVOs = new ArrayList<>();
                for (MealPlanDishEntity pd : planDishes) {
                    DishEntity dish = dishMapper.selectById(pd.getDishId());
                    if (dish != null) {
                        DishSimpleVO dvo = new DishSimpleVO();
                        dvo.setId(dish.getId());
                        dvo.setName(dish.getName());
                        dvo.setCalories(dish.getCalories());
                        dvo.setIngredients(getIngredients(dish.getId()));
                        dishSimpleVOs.add(dvo);
                        totalCalories += dish.getCalories() != null ? dish.getCalories() : 0;
                    }
                }

                mealVO.setDishes(dishSimpleVOs);
                meals.add(mealVO);
            }

            dayVO.setMeals(meals);
            dayVO.setTotalCalories(totalCalories);
            weekPlan.add(dayVO);
        }

        return weekPlan;
    }

    @Override
    public List<Map<String, Object>> getMonthPlan(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        LambdaQueryWrapper<MealPlanEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MealPlanEntity::getUserId, userId)
                .ge(MealPlanEntity::getDate, start)
                .le(MealPlanEntity::getDate, end);
        List<MealPlanEntity> plans = mealPlanMapper.selectList(wrapper);

        // Get all plan IDs
        List<Long> planIds = plans.stream().map(MealPlanEntity::getId).collect(Collectors.toList());

        // Get all plan dishes
        Map<Long, List<MealPlanDishEntity>> planDishMap = new HashMap<>();
        if (!planIds.isEmpty()) {
            LambdaQueryWrapper<MealPlanDishEntity> pdWrapper = new LambdaQueryWrapper<>();
            pdWrapper.in(MealPlanDishEntity::getMealPlanId, planIds);
            List<MealPlanDishEntity> allPlanDishes = mealPlanDishMapper.selectList(pdWrapper);
            planDishMap = allPlanDishes.stream()
                    .collect(Collectors.groupingBy(MealPlanDishEntity::getMealPlanId));
        }

        // Calculate daily calories
        Map<String, Integer> dailyCalories = new LinkedHashMap<>();
        for (MealPlanEntity plan : plans) {
            String dateKey = plan.getDate().toString();
            int calories = 0;
            List<MealPlanDishEntity> pds = planDishMap.getOrDefault(plan.getId(), new ArrayList<>());
            for (MealPlanDishEntity pd : pds) {
                DishEntity dish = dishMapper.selectById(pd.getDishId());
                if (dish != null && dish.getCalories() != null) {
                    calories += dish.getCalories();
                }
            }
            dailyCalories.merge(dateKey, calories, Integer::sum);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        dailyCalories.forEach((date, calories) -> {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("date", date);
            item.put("calories", calories);
            result.add(item);
        });

        return result;
    }

    @Override
    @Transactional
    public void createMealPlan(Long userId, MealPlanDTO dto) {
        MealPlanEntity plan = new MealPlanEntity();
        plan.setUserId(userId);
        plan.setDate(LocalDate.parse(dto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        plan.setMealType(dto.getMealType());
        plan.setCreatedAt(LocalDateTime.now());
        plan.setUpdatedAt(LocalDateTime.now());
        mealPlanMapper.insert(plan);

        if (dto.getDishIds() != null) {
            for (Long dishId : dto.getDishIds()) {
                MealPlanDishEntity mpd = new MealPlanDishEntity();
                mpd.setMealPlanId(plan.getId());
                mpd.setDishId(dishId);
                mpd.setCreatedAt(LocalDateTime.now());
                mealPlanDishMapper.insert(mpd);
            }
        }
    }

    @Override
    public List<Map<String, Object>> getDayPlan(Long userId, String date) {
        LocalDate targetDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

        LambdaQueryWrapper<MealPlanEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MealPlanEntity::getUserId, userId)
                .eq(MealPlanEntity::getDate, targetDate)
                .orderByAsc(MealPlanEntity::getMealType);
        List<MealPlanEntity> plans = mealPlanMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (MealPlanEntity plan : plans) {
            Map<String, Object> meal = new LinkedHashMap<>();
            meal.put("mealPlanId", plan.getId());
            meal.put("mealType", plan.getMealType());

            // Get dishes
            LambdaQueryWrapper<MealPlanDishEntity> mpdWrapper = new LambdaQueryWrapper<>();
            mpdWrapper.eq(MealPlanDishEntity::getMealPlanId, plan.getId());
            List<MealPlanDishEntity> planDishes = mealPlanDishMapper.selectList(mpdWrapper);

            List<Map<String, Object>> dishDetails = new ArrayList<>();
            for (MealPlanDishEntity pd : planDishes) {
                DishEntity dish = dishMapper.selectById(pd.getDishId());
                if (dish != null) {
                    Map<String, Object> dishMap = new LinkedHashMap<>();
                    dishMap.put("id", dish.getId());
                    dishMap.put("name", dish.getName());
                    dishMap.put("calories", dish.getCalories());
                    dishMap.put("ingredients", getIngredients(dish.getId()));
                    dishDetails.add(dishMap);
                }
            }

            meal.put("dishes", dishDetails);
            result.add(meal);
        }

        return result;
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
}
