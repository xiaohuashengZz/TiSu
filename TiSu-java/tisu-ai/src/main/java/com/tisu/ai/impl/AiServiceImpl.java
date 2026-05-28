package com.tisu.ai.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.ai.AiService;
import com.tisu.common.exception.BusinessException;
import com.tisu.dao.mapper.*;
import com.tisu.model.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final UserMapper userMapper;
    private final UserProfileMapper userProfileMapper;
    private final UserSettingsMapper userSettingsMapper;
    private final MealPlanMapper mealPlanMapper;
    private final MealPlanDishMapper mealPlanDishMapper;
    private final DishMapper dishMapper;
    private final WeightLogMapper weightLogMapper;
    private final TrainingLogMapper trainingLogMapper;
    private final AiReportMapper aiReportMapper;

    @Override
    public String analyze(Long userId, String type, String periodStart, String periodEnd) {
        // Get user settings for AI configuration
        LambdaQueryWrapper<UserSettingsEntity> settingsWrapper = new LambdaQueryWrapper<>();
        settingsWrapper.eq(UserSettingsEntity::getUserId, userId);
        UserSettingsEntity settings = userSettingsMapper.selectOne(settingsWrapper);

        if (settings == null || settings.getAiApiKey() == null || settings.getAiApiKey().isEmpty()) {
            throw new BusinessException(400, "请先配置AI API密钥");
        }

        // Gather user data for analysis
        String userData = gatherUserData(userId, type, periodStart, periodEnd);

        // Call external AI API
        String prompt = buildPrompt(type, userData);
        String aiResponse = callAiApi(settings.getAiApiKey(), settings.getAiModel(), prompt);

        // Save report
        AiReportEntity report = new AiReportEntity();
        report.setUserId(userId);
        report.setTitle(type + "分析报告");
        report.setType(type);
        report.setPeriodStart(LocalDate.parse(periodStart, DateTimeFormatter.ISO_LOCAL_DATE));
        report.setPeriodEnd(LocalDate.parse(periodEnd, DateTimeFormatter.ISO_LOCAL_DATE));
        report.setContent(aiResponse);
        report.setCreatedAt(LocalDateTime.now());
        aiReportMapper.insert(report);

        return aiResponse;
    }

    @Override
    public List<AiReportEntity> listReports(Long userId) {
        LambdaQueryWrapper<AiReportEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AiReportEntity::getUserId, userId)
                .orderByDesc(AiReportEntity::getCreatedAt);
        return aiReportMapper.selectList(wrapper);
    }

    @Override
    public boolean testConnection(String apiKey, String model, String apiUrl) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model);
            body.put("messages", new Object[]{
                    Map.of("role", "user", "content", "Hello, this is a connection test.")
            });
            body.put("max_tokens", 10);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(apiUrl, request, String.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    private String gatherUserData(Long userId, String type, String periodStart, String periodEnd) {
        StringBuilder data = new StringBuilder();
        LocalDate start = LocalDate.parse(periodStart, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate end = LocalDate.parse(periodEnd, DateTimeFormatter.ISO_LOCAL_DATE);

        // Get user profile
        LambdaQueryWrapper<UserProfileEntity> profileWrapper = new LambdaQueryWrapper<>();
        profileWrapper.eq(UserProfileEntity::getUserId, userId);
        UserProfileEntity profile = userProfileMapper.selectOne(profileWrapper);
        if (profile != null) {
            data.append("用户信息: ").append(profile.getGender()).append(", ")
                    .append(profile.getAge()).append("岁, ")
                    .append("身高").append(profile.getHeight()).append("cm, ")
                    .append("当前体重").append(profile.getCurrentWeight()).append("kg, ")
                    .append("目标体重").append(targetWeight(profile)).append("kg, ")
                    .append("健身目标: ").append(profile.getFitnessGoal()).append("\n");
        }

        // Get meal data
        LambdaQueryWrapper<MealPlanEntity> mealWrapper = new LambdaQueryWrapper<>();
        mealWrapper.eq(MealPlanEntity::getUserId, userId)
                .ge(MealPlanEntity::getDate, start)
                .le(MealPlanEntity::getDate, end);
        List<MealPlanEntity> mealPlans = mealPlanMapper.selectList(mealWrapper);
        data.append("饮食记录: ").append(mealPlans.size()).append("条\n");

        // Get weight data
        LambdaQueryWrapper<WeightLogEntity> weightWrapper = new LambdaQueryWrapper<>();
        weightWrapper.eq(WeightLogEntity::getUserId, userId)
                .ge(WeightLogEntity::getDate, start)
                .le(WeightLogEntity::getDate, end)
                .orderByAsc(WeightLogEntity::getDate);
        List<WeightLogEntity> weightLogs = weightLogMapper.selectList(weightWrapper);
        if (!weightLogs.isEmpty()) {
            data.append("体重变化: ").append(weightLogs.get(0).getWeight()).append("kg -> ")
                    .append(weightLogs.get(weightLogs.size() - 1).getWeight()).append("kg\n");
        }

        // Get training data
        LambdaQueryWrapper<TrainingLogEntity> trainingWrapper = new LambdaQueryWrapper<>();
        trainingWrapper.eq(TrainingLogEntity::getUserId, userId)
                .ge(TrainingLogEntity::getDate, start)
                .le(TrainingLogEntity::getDate, end);
        List<TrainingLogEntity> trainingLogs = trainingLogMapper.selectList(trainingWrapper);
        data.append("训练记录: ").append(trainingLogs.size()).append("次\n");

        return data.toString();
    }

    private String buildPrompt(String type, String userData) {
        return "你是一个专业的健身和营养顾问。请根据以下用户数据，提供" + type + "分析和建议。\n\n" +
                userData + "\n请提供详细的分析和具体的建议。";
    }

    private String callAiApi(String apiKey, String model, String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            Map<String, Object> body = new HashMap<>();
            body.put("model", model != null ? model : "gpt-3.5-turbo");
            body.put("messages", new Object[]{
                    Map.of("role", "system", "content", "你是一个专业的健身和营养顾问。"),
                    Map.of("role", "user", "content", prompt)
            });
            body.put("max_tokens", 2000);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    "https://api.openai.com/v1/chat/completions", request, Map.class);

            if (response.getBody() != null && response.getBody().containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.getBody().get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> message = (Map<String, Object>) choices.get(0).get("message");
                    return (String) message.get("content");
                }
            }

            throw new BusinessException(500, "AI返回格式异常");
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(500, "调用AI接口失败: " + e.getMessage());
        }
    }

    private String targetWeight(UserProfileEntity profile) {
        return profile.getTargetWeight() != null ? profile.getTargetWeight().toString() : "未设置";
    }
}
