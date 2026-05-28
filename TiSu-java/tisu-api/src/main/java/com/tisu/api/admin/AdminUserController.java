package com.tisu.api.admin;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.dto.AdminUserStatusDTO;
import com.tisu.model.vo.AdminUserInfoVO;
import com.tisu.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    public Result<PageResult<AdminUserInfoVO>> list(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminUserService.listUsers(status, keyword, page, size));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestBody AdminUserStatusDTO dto) {
        adminUserService.updateUserStatus(id, dto.getStatus());
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return Result.success();
    }
}
