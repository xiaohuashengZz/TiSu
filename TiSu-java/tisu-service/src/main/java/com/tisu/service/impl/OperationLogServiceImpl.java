package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tisu.common.result.PageResult;
import com.tisu.dao.mapper.OperationLogMapper;
import com.tisu.model.entity.OperationLogEntity;
import com.tisu.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogMapper operationLogMapper;

    @Override
    @Transactional
    public void log(Long adminId, String module, String action, Long targetId, String targetName, String detail, String ip) {
        OperationLogEntity log = new OperationLogEntity();
        log.setAdminId(adminId);
        log.setModule(module);
        log.setAction(action);
        log.setTargetId(targetId);
        log.setTargetName(targetName);
        log.setDetail(detail);
        log.setIp(ip);
        log.setCreatedAt(LocalDateTime.now());
        operationLogMapper.insert(log);
    }

    @Override
    public PageResult<OperationLogEntity> listLogs(Long adminId, String module, String action, int page, int size) {
        Page<OperationLogEntity> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<OperationLogEntity> wrapper = new LambdaQueryWrapper<>();

        if (adminId != null) {
            wrapper.eq(OperationLogEntity::getAdminId, adminId);
        }
        if (StringUtils.hasText(module)) {
            wrapper.eq(OperationLogEntity::getModule, module);
        }
        if (StringUtils.hasText(action)) {
            wrapper.eq(OperationLogEntity::getAction, action);
        }
        wrapper.orderByDesc(OperationLogEntity::getCreatedAt);

        Page<OperationLogEntity> result = operationLogMapper.selectPage(pageParam, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), result.getCurrent(), result.getSize());
    }
}
