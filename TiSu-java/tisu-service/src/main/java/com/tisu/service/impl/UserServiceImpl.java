package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.dao.mapper.UserMapper;
import com.tisu.dao.mapper.UserProfileMapper;
import com.tisu.dao.mapper.UserSettingsMapper;
import com.tisu.model.dto.UserProfileDTO;
import com.tisu.model.dto.UserSettingsDTO;
import com.tisu.model.entity.UserEntity;
import com.tisu.model.entity.UserProfileEntity;
import com.tisu.model.entity.UserSettingsEntity;
import com.tisu.model.vo.UserProfileVO;
import com.tisu.model.vo.UserSettingsVO;
import com.tisu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserSettingsMapper userSettingsMapper;

    @Override
    public UserProfileVO getProfile(Long userId) {
        UserEntity user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }

        LambdaQueryWrapper<UserProfileEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfileEntity::getUserId, userId);
        UserProfileEntity profile = userProfileMapper.selectOne(wrapper);

        UserProfileVO vo = new UserProfileVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setEmail(user.getEmail());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());

        if (profile != null) {
            vo.setGender(profile.getGender());
            vo.setAge(profile.getAge());
            vo.setHeight(profile.getHeight());
            vo.setCurrentWeight(profile.getCurrentWeight());
            vo.setTargetWeight(profile.getTargetWeight());
            vo.setBodyFat(profile.getBodyFat());
            vo.setFitnessGoal(profile.getFitnessGoal());
            vo.setActivityLevel(profile.getActivityLevel());
            vo.setDailyCalorieTarget(profile.getDailyCalorieTarget());
        }

        return vo;
    }

    @Override
    @Transactional
    public void updateProfile(Long userId, UserProfileDTO dto) {
        LambdaQueryWrapper<UserProfileEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserProfileEntity::getUserId, userId);
        UserProfileEntity profile = userProfileMapper.selectOne(wrapper);

        if (profile == null) {
            profile = new UserProfileEntity();
            profile.setUserId(userId);
            profile.setGender(dto.getGender());
            profile.setAge(dto.getAge());
            profile.setHeight(dto.getHeight());
            profile.setCurrentWeight(dto.getCurrentWeight());
            profile.setTargetWeight(dto.getTargetWeight());
            profile.setBodyFat(dto.getBodyFat());
            profile.setFitnessGoal(dto.getFitnessGoal());
            profile.setActivityLevel(dto.getActivityLevel());
            profile.setCreatedAt(LocalDateTime.now());
            profile.setUpdatedAt(LocalDateTime.now());
            userProfileMapper.insert(profile);
        } else {
            profile.setGender(dto.getGender());
            profile.setAge(dto.getAge());
            profile.setHeight(dto.getHeight());
            profile.setCurrentWeight(dto.getCurrentWeight());
            profile.setTargetWeight(dto.getTargetWeight());
            profile.setBodyFat(dto.getBodyFat());
            profile.setFitnessGoal(dto.getFitnessGoal());
            profile.setActivityLevel(dto.getActivityLevel());
            profile.setUpdatedAt(LocalDateTime.now());
            userProfileMapper.updateById(profile);
        }
    }

    @Override
    public UserSettingsVO getSettings(Long userId) {
        LambdaQueryWrapper<UserSettingsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSettingsEntity::getUserId, userId);
        UserSettingsEntity settings = userSettingsMapper.selectOne(wrapper);

        UserSettingsVO vo = new UserSettingsVO();
        if (settings != null) {
            // Mask API key: show only last 4 characters
            String apiKey = settings.getAiApiKey();
            if (apiKey != null && apiKey.length() > 4) {
                vo.setAiApiKey("****" + apiKey.substring(apiKey.length() - 4));
            } else {
                vo.setAiApiKey(apiKey);
            }
            vo.setAiModel(settings.getAiModel());
            vo.setUnitPreference(settings.getUnitPreference());
            vo.setNotificationEnabled(settings.getNotificationEnabled());
        }
        return vo;
    }

    @Override
    @Transactional
    public void updateSettings(Long userId, UserSettingsDTO dto) {
        LambdaQueryWrapper<UserSettingsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSettingsEntity::getUserId, userId);
        UserSettingsEntity settings = userSettingsMapper.selectOne(wrapper);

        if (settings == null) {
            settings = new UserSettingsEntity();
            settings.setUserId(userId);
            settings.setAiApiKey(dto.getAiApiKey());
            settings.setAiModel(dto.getAiModel());
            settings.setUnitPreference(dto.getUnitPreference());
            settings.setNotificationEnabled(dto.getNotificationEnabled());
            settings.setCreatedAt(LocalDateTime.now());
            settings.setUpdatedAt(LocalDateTime.now());
            userSettingsMapper.insert(settings);
        } else {
            settings.setAiApiKey(dto.getAiApiKey());
            settings.setAiModel(dto.getAiModel());
            settings.setUnitPreference(dto.getUnitPreference());
            settings.setNotificationEnabled(dto.getNotificationEnabled());
            settings.setUpdatedAt(LocalDateTime.now());
            userSettingsMapper.updateById(settings);
        }
    }
}
