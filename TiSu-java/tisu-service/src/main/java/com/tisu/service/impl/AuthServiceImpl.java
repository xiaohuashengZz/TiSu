package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.utils.BCryptUtils;
import com.tisu.common.utils.JwtUtils;
import com.tisu.dao.mapper.UserMapper;
import com.tisu.dao.mapper.UserProfileMapper;
import com.tisu.dao.mapper.UserSettingsMapper;
import com.tisu.model.dto.LoginDTO;
import com.tisu.model.dto.RegisterDTO;
import com.tisu.model.entity.UserEntity;
import com.tisu.model.entity.UserProfileEntity;
import com.tisu.model.entity.UserSettingsEntity;
import com.tisu.model.vo.LoginVO;
import com.tisu.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserSettingsMapper userSettingsMapper;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public LoginVO register(RegisterDTO dto) {
        // Check username uniqueness
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUsername, dto.getUsername());
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }

        // Create user entity
        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPassword(BCryptUtils.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname() != null ? dto.getNickname() : dto.getUsername());
        user.setStatus(1);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.insert(user);

        // Create default profile
        UserProfileEntity profile = new UserProfileEntity();
        profile.setUserId(user.getId());
        profile.setCreatedAt(LocalDateTime.now());
        profile.setUpdatedAt(LocalDateTime.now());
        userProfileMapper.insert(profile);

        // Create default settings
        UserSettingsEntity settings = new UserSettingsEntity();
        settings.setUserId(user.getId());
        settings.setCreatedAt(LocalDateTime.now());
        settings.setUpdatedAt(LocalDateTime.now());
        userSettingsMapper.insert(settings);

        // Generate JWT
        String token = jwtUtils.generateUserToken(user.getId());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setNickname(user.getNickname());
        return vo;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        // Find user by username
        LambdaQueryWrapper<UserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserEntity::getUsername, dto.getUsername());
        UserEntity user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // Verify password
        if (!BCryptUtils.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // Check status
        if (user.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }

        // Generate JWT
        String token = jwtUtils.generateUserToken(user.getId());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setNickname(user.getNickname());
        return vo;
    }
}
