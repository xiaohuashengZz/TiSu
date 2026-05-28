package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.result.PageResult;
import com.tisu.dao.mapper.*;
import com.tisu.model.entity.*;
import com.tisu.model.vo.AdminUserInfoVO;
import com.tisu.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserSettingsMapper userSettingsMapper;
    private final MealPlanMapper mealPlanMapper;
    private final MealPlanDishMapper mealPlanDishMapper;
    private final WeightLogMapper weightLogMapper;
    private final BodyMeasurementMapper bodyMeasurementMapper;
    private final TrainingLogMapper trainingLogMapper;
    private final TrainingLogDetailMapper trainingLogDetailMapper;

    @Override
    public PageResult<AdminUserInfoVO> listUsers(String status, String keyword, int page, int size) {
        Page<UserEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(status)) {
            wrapper.eq(UserEntity::getStatus, Integer.valueOf(status));
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(UserEntity::getUsername, keyword)
                    .or()
                    .like(UserEntity::getNickname, keyword)
                    .or()
                    .like(UserEntity::getEmail, keyword));
        }
        wrapper.orderByDesc(UserEntity::getCreatedAt);

        Page<UserEntity> result = userMapper.selectPage(pageParam, wrapper);

        // Get profiles for these users
        List<Long> userIds = result.getRecords().stream()
                .map(UserEntity::getId)
                .collect(Collectors.toList());

        LambdaQueryWrapper<UserProfileEntity> profileWrapper = new LambdaQueryWrapper<>();
        profileWrapper.in(UserProfileEntity::getUserId, userIds);
        List<UserProfileEntity> profiles = userProfileMapper.selectList(profileWrapper);
        var profileMap = profiles.stream()
                .collect(Collectors.toMap(UserProfileEntity::getUserId, p -> p));

        List<AdminUserInfoVO> records = result.getRecords().stream().map(user -> {
            AdminUserInfoVO vo = new AdminUserInfoVO();
            vo.setId(user.getId());
            vo.setNickname(user.getNickname());
            vo.setEmail(user.getEmail());
            vo.setRegisterDate(user.getCreatedAt() != null
                    ? user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "");

            UserProfileEntity profile = profileMap.get(user.getId());
            vo.setGoal(profile != null ? profile.getFitnessGoal() : null);
            vo.setStatus(user.getStatus() == 1 ? "正常" : "禁用");

            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(records, result.getTotal(), result.getCurrent(), result.getSize());
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, Integer status) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        user.setStatus(status);
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        // Delete user profile
        LambdaQueryWrapper<UserProfileEntity> profileWrapper = new LambdaQueryWrapper<>();
        profileWrapper.eq(UserProfileEntity::getUserId, userId);
        userProfileMapper.delete(profileWrapper);

        // Delete user settings
        LambdaQueryWrapper<UserSettingsEntity> settingsWrapper = new LambdaQueryWrapper<>();
        settingsWrapper.eq(UserSettingsEntity::getUserId, userId);
        userSettingsMapper.delete(settingsWrapper);

        // Delete meal plans and meal plan dishes
        LambdaQueryWrapper<MealPlanEntity> mealPlanWrapper = new LambdaQueryWrapper<>();
        mealPlanWrapper.eq(MealPlanEntity::getUserId, userId);
        List<MealPlanEntity> mealPlans = mealPlanMapper.selectList(mealPlanWrapper);
        for (MealPlanEntity plan : mealPlans) {
            LambdaQueryWrapper<MealPlanDishEntity> mpdWrapper = new LambdaQueryWrapper<>();
            mpdWrapper.eq(MealPlanDishEntity::getMealPlanId, plan.getId());
            mealPlanDishMapper.delete(mpdWrapper);
        }
        mealPlanMapper.delete(mealPlanWrapper);

        // Delete weight logs
        LambdaQueryWrapper<WeightLogEntity> weightWrapper = new LambdaQueryWrapper<>();
        weightWrapper.eq(WeightLogEntity::getUserId, userId);
        weightLogMapper.delete(weightWrapper);

        // Delete body measurements
        LambdaQueryWrapper<BodyMeasurementEntity> bodyWrapper = new LambdaQueryWrapper<>();
        bodyWrapper.eq(BodyMeasurementEntity::getUserId, userId);
        bodyMeasurementMapper.delete(bodyWrapper);

        // Delete training logs and details
        LambdaQueryWrapper<TrainingLogEntity> trainingLogWrapper = new LambdaQueryWrapper<>();
        trainingLogWrapper.eq(TrainingLogEntity::getUserId, userId);
        List<TrainingLogEntity> trainingLogs = trainingLogMapper.selectList(trainingLogWrapper);
        for (TrainingLogEntity log : trainingLogs) {
            LambdaQueryWrapper<TrainingLogDetailEntity> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(TrainingLogDetailEntity::getLogId, log.getId());
            trainingLogDetailMapper.delete(detailWrapper);
        }
        trainingLogMapper.delete(trainingLogWrapper);

        // Delete user
        userMapper.deleteById(userId);
    }
}
