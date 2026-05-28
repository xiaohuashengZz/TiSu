package com.tisu.api.admin;

import com.tisu.common.result.PageResult;
import com.tisu.common.result.Result;
import com.tisu.model.entity.OperationLogEntity;
import com.tisu.service.AdminOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/operation-logs")
@RequiredArgsConstructor
public class AdminOperationLogController {

    private final AdminOperationLogService adminOperationLogService;

    @GetMapping
    public Result<PageResult<OperationLogEntity>> list(
            @RequestParam(required = false) Long adminId,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(adminOperationLogService.listLogs(adminId, module, action, page, size));
    }
}
