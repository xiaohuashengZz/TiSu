package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.TrainingLogDTO;
import com.tisu.service.TrainingLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/training-logs")
@RequiredArgsConstructor
public class TrainingLogController {

    private final TrainingLogService trainingLogService;

    @GetMapping
    public Result<List<Map<String, Object>>> list(HttpServletRequest request,
                                                  @RequestParam(required = false) String startDate,
                                                  @RequestParam(required = false) String endDate) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(trainingLogService.listLogs(userId, startDate, endDate));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody TrainingLogDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        trainingLogService.createLog(userId, dto);
        return Result.success();
    }
}
