package com.tisu.service;

import com.tisu.model.dto.BodyMeasurementDTO;
import com.tisu.model.entity.BodyMeasurementEntity;

import java.util.List;

public interface BodyMeasurementService {

    List<BodyMeasurementEntity> listLogs(Long userId, String startDate, String endDate);

    void createLog(Long userId, BodyMeasurementDTO dto);
}
