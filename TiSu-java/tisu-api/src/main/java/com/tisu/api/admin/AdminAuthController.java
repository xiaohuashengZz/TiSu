package com.tisu.api.admin;

import com.tisu.common.result.Result;
import com.tisu.model.dto.AdminLoginDTO;
import com.tisu.model.vo.AdminLoginVO;
import com.tisu.model.vo.AdminUserInfoVO;
import com.tisu.service.AdminAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/login")
    public Result<AdminLoginVO> login(@RequestBody AdminLoginDTO dto) {
        return Result.success(adminAuthService.login(dto));
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        return Result.success();
    }

    @GetMapping("/info")
    public Result<AdminUserInfoVO> getAdminInfo(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("adminId");
        return Result.success(adminAuthService.getAdminInfo(adminId));
    }
}
