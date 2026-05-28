package com.tisu.service;

import com.tisu.model.dto.AdminLoginDTO;
import com.tisu.model.vo.AdminLoginVO;
import com.tisu.model.vo.AdminUserInfoVO;

public interface AdminAuthService {

    AdminLoginVO login(AdminLoginDTO dto);

    AdminUserInfoVO getAdminInfo(Long adminId);
}
