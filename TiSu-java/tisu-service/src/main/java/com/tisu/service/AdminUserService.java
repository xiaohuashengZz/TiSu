package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.vo.AdminUserInfoVO;

public interface AdminUserService {

    PageResult<AdminUserInfoVO> listUsers(String status, String keyword, int page, int size);

    void updateUserStatus(Long userId, Integer status);

    void deleteUser(Long userId);
}
