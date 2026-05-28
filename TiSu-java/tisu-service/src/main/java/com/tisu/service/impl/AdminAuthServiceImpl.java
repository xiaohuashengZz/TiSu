package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.common.utils.BCryptUtils;
import com.tisu.common.utils.JwtUtils;
import com.tisu.dao.mapper.AdminUserMapper;
import com.tisu.model.dto.AdminLoginDTO;
import com.tisu.model.entity.AdminUserEntity;
import com.tisu.model.vo.AdminLoginVO;
import com.tisu.model.vo.AdminUserInfoVO;
import com.tisu.service.AdminAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class AdminAuthServiceImpl implements AdminAuthService {

    private final AdminUserMapper adminUserMapper;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public AdminLoginVO login(AdminLoginDTO dto) {
        // Find admin by username
        LambdaQueryWrapper<AdminUserEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserEntity::getUsername, dto.getUsername());
        AdminUserEntity admin = adminUserMapper.selectOne(wrapper);
        if (admin == null) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // Verify password
        if (!BCryptUtils.matches(dto.getPassword(), admin.getPassword())) {
            throw new BusinessException(400, "用户名或密码错误");
        }

        // Check status
        if (admin.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }

        // Update last login time
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        adminUserMapper.updateById(admin);

        // Generate admin JWT
        String token = jwtUtils.generateAdminToken(admin.getId());

        AdminLoginVO vo = new AdminLoginVO();
        vo.setToken(token);
        vo.setAdminId(admin.getId());
        vo.setNickname(admin.getNickname());
        vo.setRole(admin.getRole());
        return vo;
    }

    @Override
    public AdminUserInfoVO getAdminInfo(Long adminId) {
        AdminUserEntity admin = adminUserMapper.selectById(adminId);
        if (admin == null) {
            throw new BusinessException(404, "管理员不存在");
        }

        AdminUserInfoVO vo = new AdminUserInfoVO();
        vo.setId(admin.getId());
        vo.setNickname(admin.getNickname());
        vo.setRole(admin.getRole());
        return vo;
    }
}
