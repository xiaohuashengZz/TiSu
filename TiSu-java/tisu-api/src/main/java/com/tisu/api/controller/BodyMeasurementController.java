package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.BodyMeasurementDTO;
import com.tisu.model.entity.BodyMeasurementEntity;
import com.tisu.service.BodyMeasurementService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/body-measurements")
@RequiredArgsConstructor
public class BodyMeasurementController {

    private final BodyMeasurementService bodyMeasurementService;

    @GetMapping
    public Result<List<BodyMeasurementEntity>> list(HttpServletRequest request,
                                                    @RequestParam(required = false) String startDate,
                                                    @RequestParam(required = false) String endDate) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(bodyMeasurementService.listLogs(userId, startDate, endDate));
    }

    @PostMapping
    public Result<Void> create(HttpServletRequest request, @RequestBody BodyMeasurementDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        bodyMeasurementService.createLog(userId, dto);
        return Result.success();
    }
}
