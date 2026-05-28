package com.tisu.service;

import java.util.List;
import java.util.Map;

public interface TrainingLogService {

    List<Map<String, Object>> listLogs(Long userId, String startDate, String endDate);

    void createLog(Long userId, Map<String, Object> dto);
}
