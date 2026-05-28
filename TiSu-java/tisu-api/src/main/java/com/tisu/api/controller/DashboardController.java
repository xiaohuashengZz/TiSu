package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.vo.DashboardVO;
import com.tisu.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public Result<DashboardVO> getDashboard(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(dashboardService.getDashboard(userId));
    }
}
