package com.tisu.service;

import com.tisu.common.result.PageResult;
import com.tisu.model.entity.OperationLogEntity;

public interface OperationLogService {

    void log(Long adminId, String module, String action, Long targetId, String targetName, String detail, String ip);

    PageResult<OperationLogEntity> listLogs(Long adminId, String module, String action, int page, int size);
}
