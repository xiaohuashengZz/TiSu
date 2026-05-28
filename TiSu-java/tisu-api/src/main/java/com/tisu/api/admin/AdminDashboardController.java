package com.tisu.api.admin;

import com.tisu.common.result.Result;
import com.tisu.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats() {
        return Result.success(adminDashboardService.getStats());
    }

    @GetMapping("/activities")
    public Result<List<Map<String, Object>>> getActivities(
            @RequestParam(defaultValue = "10") Integer limit) {
        return Result.success(adminDashboardService.getActivities(limit));
    }

    @GetMapping("/overview")
    public Result<Map<String, Object>> getOverview() {
        return Result.success(adminDashboardService.getOverview());
    }
}
