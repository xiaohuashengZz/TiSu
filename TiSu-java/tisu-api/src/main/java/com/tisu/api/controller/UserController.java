package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.UserProfileDTO;
import com.tisu.model.dto.UserSettingsDTO;
import com.tisu.model.vo.UserProfileVO;
import com.tisu.model.vo.UserSettingsVO;
import com.tisu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getProfile(userId));
    }

    @PutMapping("/profile")
    public Result<Void> updateProfile(HttpServletRequest request, @RequestBody UserProfileDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateProfile(userId, dto);
        return Result.success();
    }

    @GetMapping("/settings")
    public Result<UserSettingsVO> getSettings(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(userService.getSettings(userId));
    }

    @PutMapping("/settings")
    public Result<Void> updateSettings(HttpServletRequest request, @RequestBody UserSettingsDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        userService.updateSettings(userId, dto);
        return Result.success();
    }
}
