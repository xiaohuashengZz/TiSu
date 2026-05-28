package com.tisu.service;

import java.util.List;
import java.util.Map;

public interface AdminDashboardService {

    Map<String, Object> getStats();

    List<Map<String, Object>> getActivities(int limit);

    Map<String, Object> getOverview();
}
