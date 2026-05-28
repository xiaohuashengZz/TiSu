package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.dao.mapper.*;
import com.tisu.model.entity.*;
import com.tisu.model.vo.DashboardVO;
import com.tisu.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final MealPlanMapper mealPlanMapper;
    private final MealPlanDishMapper mealPlanDishMapper;
    private final DishMapper dishMapper;
    private final UserProfileMapper userProfileMapper;
    private final TrainingLogMapper trainingLogMapper;
    private final WeightLogMapper weightLogMapper;

    @Override
    public DashboardVO getDashboard(Long userId) {
        DashboardVO vo = new DashboardVO();
        LocalDate today = LocalDate.now();

        // Today's calories from meal plans
        LambdaQueryWrapper<MealPlanEntity> mealWrapper = new LambdaQueryWrapper<>();
        mealWrapper.eq(MealPlanEntity::getUserId, userId)
                .eq(MealPlanEntity::getDate, today);
        List<MealPlanEntity> todayMeals = mealPlanMapper.selectList(mealWrapper);

        int todayCalories = 0;
        List<Long> planIds = todayMeals.stream().map(MealPlanEntity::getId).collect(Collectors.toList());
        if (!planIds.isEmpty()) {
            LambdaQueryWrapper<MealPlanDishEntity> mpdWrapper = new LambdaQueryWrapper<>();
            mpdWrapper.in(MealPlanDishEntity::getMealPlanId, planIds);
            List<MealPlanDishEntity> planDishes = mealPlanDishMapper.selectList(mpdWrapper);

            for (MealPlanDishEntity pd : planDishes) {
                DishEntity dish = dishMapper.selectById(pd.getDishId());
                if (dish != null && dish.getCalories() != null) {
                    todayCalories += dish.getCalories();
                }
            }
        }
        vo.setTodayCalories(todayCalories);

        // Today's target from profile
        LambdaQueryWrapper<UserProfileEntity> profileWrapper = new LambdaQueryWrapper<>();
        profileWrapper.eq(UserProfileEntity::getUserId, userId);
        UserProfileEntity profile = userProfileMapper.selectOne(profileWrapper);
        vo.setTodayTarget(profile != null && profile.getDailyCalorieTarget() != null
                ? profile.getDailyCalorieTarget() : 2000);

        // Today's burned calories
        LambdaQueryWrapper<TrainingLogEntity> todayTrainingWrapper = new LambdaQueryWrapper<>();
        todayTrainingWrapper.eq(TrainingLogEntity::getUserId, userId)
                .eq(TrainingLogEntity::getDate, today);
        List<TrainingLogEntity> todayTrainings = trainingLogMapper.selectList(todayTrainingWrapper);
        int todayBurned = todayTrainings.stream()
                .mapToInt(t -> t.getCaloriesBurned() != null ? t.getCaloriesBurned() : 0)
                .sum();
        vo.setTodayBurned(todayBurned);

        // Week weight change
        LocalDate weekStart = today.with(DayOfWeek.MONDAY);
        LambdaQueryWrapper<WeightLogEntity> weekWeightWrapper = new LambdaQueryWrapper<>();
        weekWeightWrapper.eq(WeightLogEntity::getUserId, userId)
                .ge(WeightLogEntity::getDate, weekStart)
                .le(WeightLogEntity::getDate, today)
                .orderByAsc(WeightLogEntity::getDate);
        List<WeightLogEntity> weekWeights = weightLogMapper.selectList(weekWeightWrapper);
        if (weekWeights.size() >= 2) {
            BigDecimal first = weekWeights.get(0).getWeight();
            BigDecimal last = weekWeights.get(weekWeights.size() - 1).getWeight();
            vo.setWeekWeightChange(last.subtract(first));
        } else {
            vo.setWeekWeightChange(BigDecimal.ZERO);
        }

        // Month training days
        LocalDate monthStart = today.withDayOfMonth(1);
        LambdaQueryWrapper<TrainingLogEntity> monthTrainingWrapper = new LambdaQueryWrapper<>();
        monthTrainingWrapper.eq(TrainingLogEntity::getUserId, userId)
                .ge(TrainingLogEntity::getDate, monthStart)
                .le(TrainingLogEntity::getDate, today);
        List<TrainingLogEntity> monthTrainings = trainingLogMapper.selectList(monthTrainingWrapper);
        vo.setMonthTrainingDays(monthTrainings.size());

        // Total training minutes
        int totalMinutes = monthTrainings.stream()
                .mapToInt(t -> t.getDuration() != null ? t.getDuration() : 0)
                .sum();
        vo.setTotalTrainingMinutes(totalMinutes);

        // Recent weight logs (last 7)
        LambdaQueryWrapper<WeightLogEntity> recentWeightWrapper = new LambdaQueryWrapper<>();
        recentWeightWrapper.eq(WeightLogEntity::getUserId, userId)
                .orderByDesc(WeightLogEntity::getDate)
                .last("LIMIT 7");
        List<WeightLogEntity> recentWeights = weightLogMapper.selectList(recentWeightWrapper);
        vo.setWeightLogs(new ArrayList<>(recentWeights));

        // Weekly calories (this week)
        List<Integer> weeklyCalories = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate day = weekStart.plusDays(i);
            LambdaQueryWrapper<MealPlanEntity> dayMealWrapper = new LambdaQueryWrapper<>();
            dayMealWrapper.eq(MealPlanEntity::getUserId, userId)
                    .eq(MealPlanEntity::getDate, day);
            List<MealPlanEntity> dayMeals = mealPlanMapper.selectList(dayMealWrapper);

            int dayCalories = 0;
            List<Long> dayPlanIds = dayMeals.stream().map(MealPlanEntity::getId).collect(Collectors.toList());
            if (!dayPlanIds.isEmpty()) {
                LambdaQueryWrapper<MealPlanDishEntity> dayMpdWrapper = new LambdaQueryWrapper<>();
                dayMpdWrapper.in(MealPlanDishEntity::getMealPlanId, dayPlanIds);
                List<MealPlanDishEntity> dayPlanDishes = mealPlanDishMapper.selectList(dayMpdWrapper);
                for (MealPlanDishEntity pd : dayPlanDishes) {
                    DishEntity dish = dishMapper.selectById(pd.getDishId());
                    if (dish != null && dish.getCalories() != null) {
                        dayCalories += dish.getCalories();
                    }
                }
            }
            weeklyCalories.add(dayCalories);
        }
        vo.setWeeklyCalories(weeklyCalories);

        return vo;
    }
}
