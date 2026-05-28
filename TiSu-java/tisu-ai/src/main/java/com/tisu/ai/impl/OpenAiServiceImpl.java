package com.tisu.ai.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.ai.AiService;
import com.tisu.dao.mapper.AiReportMapper;
import com.tisu.dao.mapper.UserSettingsMapper;
import com.tisu.dao.mapper.WeightLogMapper;
import com.tisu.dao.mapper.BodyMeasurementMapper;
import com.tisu.dao.mapper.MealPlanMapper;
import com.tisu.dao.mapper.MealPlanDishMapper;
import com.tisu.model.entity.AiReportEntity;
import com.tisu.model.entity.UserSettingsEntity;
import com.tisu.model.entity.WeightLogEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAiServiceImpl implements AiService {

    private final AiReportMapper aiReportMapper;
    private final UserSettingsMapper userSettingsMapper;
    private final WeightLogMapper weightLogMapper;
    private final BodyMeasurementMapper bodyMeasurementMapper;
    private final MealPlanMapper mealPlanMapper;

    @Value("${ai.default-url:https://api.openai.com/v1/chat/completions}")
    private String defaultApiUrl;

    @Value("${ai.default-model:gpt-4o}")
    private String defaultModel;

    @Override
    public String analyze(Long userId, String type, String periodStart, String periodEnd) {
        // Get user AI settings
        LambdaQueryWrapper<UserSettingsEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserSettingsEntity::getUserId, userId);
        UserSettingsEntity settings = userSettingsMapper.selectOne(wrapper);

        String apiKey = (settings != null && settings.getAiApiKey() != null) ? settings.getAiApiKey() : "";
        String model = (settings != null && settings.getAiModel() != null) ? settings.getAiModel() : defaultModel;
        String apiUrl = defaultApiUrl;

        // Gather user data for analysis
        StringBuilder prompt = new StringBuilder();
        prompt.append("你是一个专业的健身饮食分析师。请根据以下用户数据生成分析报告。\n\n");

        // Get weight logs
        LambdaQueryWrapper<WeightLogEntity> weightWrapper = new LambdaQueryWrapper<>();
        weightWrapper.eq(WeightLogEntity::getUserId, userId);
        if (periodStart != null && !periodStart.isEmpty()) {
            weightWrapper.ge(WeightLogEntity::getDate, LocalDate.parse(periodStart));
        }
        if (periodEnd != null && !periodEnd.isEmpty()) {
            weightWrapper.le(WeightLogEntity::getDate, LocalDate.parse(periodEnd));
        }
        weightWrapper.orderByDesc(WeightLogEntity::getDate);
        List<WeightLogEntity> weightLogs = weightLogMapper.selectList(weightWrapper);

        if (!weightLogs.isEmpty()) {
            prompt.append("## 体重记录\n");
            for (WeightLogEntity w : weightLogs) {
                prompt.append(String.format("- %s: %.1fkg, 体脂率: %s%%\n",
                        w.getDate(), w.getWeight(),
                        w.getBodyFat() != null ? w.getBodyFat().toString() : "N/A"));
            }
            prompt.append("\n");
        }

        // Build the AI request
        prompt.append("请从饮食、训练、体重趋势三个方面进行分析，并给出具体建议。使用Markdown格式输出。");

        if (apiKey.isEmpty()) {
            // Return mock analysis if no API key configured
            String content = generateMockAnalysis(weightLogs, type);
            saveReport(userId, type, periodStart, periodEnd, content);
            return content;
        }

        try {
            String content = callAiApi(apiUrl, apiKey, model, prompt.toString());
            saveReport(userId, type, periodStart, periodEnd, content);
            return content;
        } catch (Exception e) {
            log.error("AI API call failed", e);
            String content = generateMockAnalysis(weightLogs, type);
            saveReport(userId, type, periodStart, periodEnd, content);
            return content;
        }
    }

    @Override
    public List<AiReportEntity> listReports(Long userId) {
        LambdaQueryWrapper<AiReportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportEntity::getUserId, userId);
        wrapper.orderByDesc(AiReportEntity::getCreatedAt);
        return aiReportMapper.selectList(wrapper);
    }

    @Override
    public boolean testConnection(String apiKey, String model, String apiUrl) {
        if (apiKey == null || apiKey.isEmpty()) {
            return false;
        }
        try {
            String url = (apiUrl != null && !apiUrl.isEmpty()) ? apiUrl : defaultApiUrl;
            String m = (model != null && !model.isEmpty()) ? model : defaultModel;
            callAiApi(url, apiKey, m, "Hello, this is a connection test.");
            return true;
        } catch (Exception e) {
            log.error("AI connection test failed", e);
            return false;
        }
    }

    private String callAiApi(String apiUrl, String apiKey, String model, String prompt) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("messages", List.of(
                Map.of("role", "system", "content", "你是一个专业的健身饮食分析师。"),
                Map.of("role", "user", "content", prompt)
        ));
        body.put("max_tokens", 2000);
        body.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

        if (response.getBody() != null && response.getBody().containsKey("choices")) {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
            if (!choices.isEmpty()) {
                Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                return (String) message.get("content");
            }
        }
        throw new RuntimeException("Invalid AI API response");
    }

    private void saveReport(Long userId, String type, String periodStart, String periodEnd, String content) {
        AiReportEntity report = new AiReportEntity();
        report.setUserId(userId);
        report.setType(type);
        report.setTitle(("weekly".equals(type) ? "周度" : "月度") + "健康分析报告");
        report.setSummary(content.length() > 200 ? content.substring(0, 200) + "..." : content);
        report.setContent(content);
        if (periodStart != null && !periodStart.isEmpty()) {
            report.setPeriodStart(LocalDate.parse(periodStart));
        }
        if (periodEnd != null && !periodEnd.isEmpty()) {
            report.setPeriodEnd(LocalDate.parse(periodEnd));
        }
        report.setCreatedAt(LocalDateTime.now());
        aiReportMapper.insert(report);
    }

    private String generateMockAnalysis(List<WeightLogEntity> weightLogs, String type) {
        StringBuilder sb = new StringBuilder();
        sb.append("## 饮食分析\n\n");
        sb.append("根据您的饮食记录，近期热量摄入总体控制良好。建议继续保持高蛋白饮食，适当增加蔬菜和膳食纤维的摄入。\n\n");
        sb.append("## 训练分析\n\n");
        sb.append("训练频率和强度保持稳定，建议适当增加力量训练的组数和重量，以促进肌肉增长和代谢提升。\n\n");
        sb.append("## 体重趋势\n\n");

        if (weightLogs.size() >= 2) {
            double latest = weightLogs.get(0).getWeight().doubleValue();
            double earliest = weightLogs.get(weightLogs.size() - 1).getWeight().doubleValue();
            double change = latest - earliest;
            sb.append(String.format("体重从 %.1fkg 变化到 %.1fkg，变化 %.1fkg。", earliest, latest, change));
            if (change < 0) {
                sb.append("减重趋势良好，请继续保持。\n\n");
            } else if (change > 0) {
                sb.append("体重有所增加，请注意控制饮食热量。\n\n");
            } else {
                sb.append("体重保持稳定。\n\n");
            }
        } else {
            sb.append("暂无足够的体重数据进行趋势分析，建议坚持每日记录体重。\n\n");
        }

        sb.append("## 建议\n\n");
        sb.append("1. 保持规律的饮食习惯，避免暴饮暴食\n");
        sb.append("2. 每周至少进行3-4次力量训练\n");
        sb.append("3. 保证充足的睡眠（7-8小时）\n");
        sb.append("4. 每天饮水量不少于2000ml");

        return sb.toString();
    }
}
