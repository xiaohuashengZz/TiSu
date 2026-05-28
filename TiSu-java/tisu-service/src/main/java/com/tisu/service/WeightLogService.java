package com.tisu.service;

import com.tisu.model.dto.WeightLogDTO;
import com.tisu.model.vo.WeightLogVO;

import java.util.List;

public interface WeightLogService {

    List<WeightLogVO> listLogs(Long userId, String startDate, String endDate);

    void createLog(Long userId, WeightLogDTO dto);
}
