package com.tisu.api.controller;

import com.tisu.common.result.Result;
import com.tisu.model.dto.AiAnalyzeDTO;
import com.tisu.model.dto.AiTestDTO;
import com.tisu.model.entity.AiReportEntity;
import com.tisu.service.AiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/analyze")
    public Result<String> analyze(HttpServletRequest request, @RequestBody AiAnalyzeDTO dto) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(aiService.analyze(userId, dto.getType(), dto.getPeriodStart(), dto.getPeriodEnd()));
    }

    @GetMapping("/reports")
    public Result<List<AiReportEntity>> listReports(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return Result.success(aiService.listReports(userId));
    }

    @PostMapping("/test-connection")
    public Result<Boolean> testConnection(@RequestBody AiTestDTO dto) {
        return Result.success(aiService.testConnection(dto.getApiKey(), dto.getModel(), dto.getApiUrl()));
    }
}
