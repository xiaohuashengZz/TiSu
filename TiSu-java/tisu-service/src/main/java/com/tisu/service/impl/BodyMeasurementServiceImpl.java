package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.dao.mapper.BodyMeasurementMapper;
import com.tisu.model.dto.BodyMeasurementDTO;
import com.tisu.model.entity.BodyMeasurementEntity;
import com.tisu.service.BodyMeasurementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BodyMeasurementServiceImpl implements BodyMeasurementService {

    private final BodyMeasurementMapper bodyMeasurementMapper;

    @Override
    public List<BodyMeasurementEntity> listLogs(Long userId, String startDate, String endDate) {
        LambdaQueryWrapper<BodyMeasurementEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BodyMeasurementEntity::getUserId, userId);
        if (startDate != null) {
            wrapper.ge(BodyMeasurementEntity::getDate, LocalDate.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (endDate != null) {
            wrapper.le(BodyMeasurementEntity::getDate, LocalDate.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE));
        }
        wrapper.orderByDesc(BodyMeasurementEntity::getDate);
        return bodyMeasurementMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void createLog(Long userId, BodyMeasurementDTO dto) {
        BodyMeasurementEntity entity = new BodyMeasurementEntity();
        entity.setUserId(userId);
        entity.setDate(LocalDate.parse(dto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE));
        entity.setChest(dto.getChest());
        entity.setWaist(dto.getWaist());
        entity.setHip(dto.getHip());
        entity.setArm(dto.getArm());
        entity.setThigh(dto.getThigh());
        entity.setCreatedAt(LocalDateTime.now());
        bodyMeasurementMapper.insert(entity);
    }
}
