package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.dao.mapper.ExerciseMapper;
import com.tisu.dao.mapper.TrainingLogDetailMapper;
import com.tisu.dao.mapper.TrainingLogMapper;
import com.tisu.model.entity.ExerciseEntity;
import com.tisu.model.entity.TrainingLogDetailEntity;
import com.tisu.model.entity.TrainingLogEntity;
import com.tisu.service.TrainingLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrainingLogServiceImpl implements TrainingLogService {

    private final TrainingLogMapper trainingLogMapper;
    private final TrainingLogDetailMapper trainingLogDetailMapper;
    private final ExerciseMapper exerciseMapper;

    @Override
    public List<Map<String, Object>> listLogs(Long userId, String startDate, String endDate) {
        LambdaQueryWrapper<TrainingLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TrainingLogEntity::getUserId, userId);
        if (startDate != null) {
            wrapper.ge(TrainingLogEntity::getDate, LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (endDate != null) {
            wrapper.le(TrainingLogEntity::getDate, LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        wrapper.orderByDesc(TrainingLogEntity::getDate);

        List<TrainingLogEntity> logs = trainingLogMapper.selectList(wrapper);

        List<Map<String, Object>> result = new ArrayList<>();
        for (TrainingLogEntity log : logs) {
            Map<String, Object> logMap = new LinkedHashMap<>();
            logMap.put("id", log.getId());
            logMap.put("date", log.getDate().toString());
            logMap.put("planId", log.getPlanId());
            logMap.put("duration", log.getDuration());
            logMap.put("notes", log.getNotes());
            logMap.put("caloriesBurned", log.getCaloriesBurned());

            // Get details
            LambdaQueryWrapper<TrainingLogDetailEntity> detailWrapper = new LambdaQueryWrapper<>();
            detailWrapper.eq(TrainingLogDetailEntity::getLogId, log.getId());
            List<TrainingLogDetailEntity> details = trainingLogDetailMapper.selectList(detailWrapper);

            List<Long> exerciseIds = details.stream()
                    .map(TrainingLogDetailEntity::getExerciseId)
                    .distinct()
                    .collect(Collectors.toList());
            Map<Long, String> exerciseNameMap = new HashMap<>();
            if (!exerciseIds.isEmpty()) {
                List<ExerciseEntity> exercises = exerciseMapper.selectBatchIds(exerciseIds);
                exerciseNameMap = exercises.stream()
                        .collect(Collectors.toMap(ExerciseEntity::getId, ExerciseEntity::getName));
            }

            List<Map<String, Object>> detailList = details.stream().map(d -> {
                Map<String, Object> dm = new LinkedHashMap<>();
                dm.put("exerciseId", d.getExerciseId());
                dm.put("exerciseName", exerciseNameMap.getOrDefault(d.getExerciseId(), ""));
                dm.put("setNumber", d.getSetNumber());
                dm.put("reps", d.getReps());
                dm.put("weight", d.getWeight());
                dm.put("completed", d.getCompleted());
                return dm;
            }).collect(Collectors.toList());

            logMap.put("details", detailList);
            result.add(logMap);
        }

        return result;
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void createLog(Long userId, Map<String, Object> dto) {
        TrainingLogEntity log = new TrainingLogEntity();
        log.setUserId(userId);
        log.setDate(LocalDate.parse((String) dto.get("date"), DateTimeFormatter.ISO_LOCAL_DATE));
        log.setPlanId(dto.get("planId") != null ? Long.valueOf(dto.get("planId").toString()) : null);
        log.setDuration(dto.get("duration") != null ? Integer.valueOf(dto.get("duration").toString()) : null);
        log.setNotes((String) dto.get("notes"));
        log.setCaloriesBurned(dto.get("caloriesBurned") != null ? Integer.valueOf(dto.get("caloriesBurned").toString()) : null);
        log.setCreatedAt(LocalDateTime.now());
        trainingLogMapper.insert(log);

        // Insert details
        if (dto.containsKey("details") && dto.get("details") instanceof List) {
            List<Map<String, Object>> details = (List<Map<String, Object>>) dto.get("details");
            for (Map<String, Object> detail : details) {
                TrainingLogDetailEntity detailEntity = new TrainingLogDetailEntity();
                detailEntity.setLogId(log.getId());
                detailEntity.setExerciseId(Long.valueOf(detail.get("exerciseId").toString()));
                detailEntity.setSetNumber(detail.get("setNumber") != null ? Integer.valueOf(detail.get("setNumber").toString()) : null);
                detailEntity.setReps(detail.get("reps") != null ? Integer.valueOf(detail.get("reps").toString()) : null);
                detailEntity.setWeight(detail.get("weight") != null ? new BigDecimal(detail.get("weight").toString()) : null);
                detailEntity.setCompleted(detail.get("completed") != null ? Integer.valueOf(detail.get("completed").toString()) : 1);
                detailEntity.setCreatedAt(LocalDateTime.now());
                trainingLogDetailMapper.insert(detailEntity);
            }
        }
    }
}
