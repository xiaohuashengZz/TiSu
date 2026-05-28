package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.common.exception.BusinessException;
import com.tisu.dao.mapper.UserProfileMapper;
import com.tisu.dao.mapper.WeightLogMapper;
import com.tisu.model.dto.WeightLogDTO;
import com.tisu.model.entity.UserProfileEntity;
import com.tisu.model.entity.WeightLogEntity;
import com.tisu.model.vo.WeightLogVO;
import com.tisu.service.WeightLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WeightLogServiceImpl implements WeightLogService {

    private final WeightLogMapper weightLogMapper;
    private final UserProfileMapper userProfileMapper;

    @Override
    public List<WeightLogVO> listLogs(Long userId, String startDate, String endDate) {
        LambdaQueryWrapper<WeightLogEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WeightLogEntity::getUserId, userId);
        if (startDate != null) {
            wrapper.ge(WeightLogEntity::getDate, LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (endDate != null) {
            wrapper.le(WeightLogEntity::getDate, LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        wrapper.orderByDesc(WeightLogEntity::getDate);

        List<WeightLogEntity> logs = weightLogMapper.selectList(wrapper);

        return logs.stream().map(log -> {
            WeightLogVO vo = new WeightLogVO();
            vo.setId(log.getId());
            vo.setDate(log.getDate());
            vo.setWeight(log.getWeight());
            vo.setBodyFat(log.getBodyFat());
            vo.setBmi(log.getBmi());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createLog(Long userId, WeightLogDTO dto) {
        // Get user height for BMI calculation
        LambdaQueryWrapper<UserProfileEntity> profileWrapper = new LambdaQueryWrapper<>();
        profileWrapper.eq(UserProfileEntity::getUserId, userId);
        UserProfileEntity profile = userProfileMapper.selectOne(profileWrapper);

        BigDecimal bmi = null;
        if (profile != null && profile.getHeight() != null && profile.getHeight().compareTo(BigDecimal.ZERO) > 0) {
            // BMI = weight / (height/100)^2
            BigDecimal heightInMeters = profile.getHeight().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
            BigDecimal heightSquared = heightInMeters.multiply(heightInMeters);
            bmi = dto.getWeight().divide(heightSquared, 1, RoundingMode.HALF_UP);
        }

        WeightLogEntity log = new WeightLogEntity();
        log.setUserId(userId);
        log.setDate(LocalDate.parse(dto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        log.setWeight(dto.getWeight());
        log.setBodyFat(dto.getBodyFat());
        log.setBmi(bmi);
        log.setCreatedAt(LocalDateTime.now());
        weightLogMapper.insert(log);
    }
}
