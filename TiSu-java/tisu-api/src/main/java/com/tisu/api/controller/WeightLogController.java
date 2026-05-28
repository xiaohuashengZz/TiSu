package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.WeightLogDTO;
import com.tisu.model.entity.WeightLogEntity;
import com.tisu.service.WeightLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weight-logs")
@RequiredArgsConstructor
public class WeightLogController {

    private final WeightLogService weightLogService;

    @GetMapping
    public Result<List<WeightLogEntity>> list(HttpServletRequest request,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(weightLogService.listLogs(userId, startDate, endDate));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody WeightLogDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        weightLogService.createLog(userId, dto);
        return Result.success();
    }
}
