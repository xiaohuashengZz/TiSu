package com.tisu.service;

import com.tisu.model.dto.UserProfileDTO;
import com.tisu.model.dto.UserSettingsDTO;
import com.tisu.model.vo.UserProfileVO;
import com.tisu.model.vo.UserSettingsVO;

public interface UserService {

    UserProfileVO getProfile(Long userId);

    void updateProfile(Long userId, UserProfileDTO dto);

    UserSettingsVO getSettings(Long userId);

    void updateSettings(Long userId, UserSettingsDTO dto);
}
