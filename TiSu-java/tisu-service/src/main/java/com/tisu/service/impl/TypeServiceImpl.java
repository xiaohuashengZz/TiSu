package com.tisu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tisu.dao.mapper.TypeMapper;
import com.tisu.model.entity.TypeEntity;
import com.tisu.service.TypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

    private final TypeMapper typeMapper;

    @Override
    public List<TypeEntity> listByGroup(String typeGroup) {
        LambdaQueryWrapper<TypeEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TypeEntity::getTypeGroup, typeGroup)
                .orderByAsc(TypeEntity::getSortOrder);
        return typeMapper.selectList(wrapper);
    }
}
