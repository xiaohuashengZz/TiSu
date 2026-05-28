package com.tisu.ai;

import com.tisu.model.entity.AiReportEntity;

import java.util.List;

public interface AiService {

    String analyze(Long userId, String type, String periodStart, String periodEnd);

    List<AiReportEntity> listReports(Long userId);

    boolean testConnection(String apiKey, String model, String apiUrl);
}
