package com.tisu.service;

import com.tisu.model.entity.TypeEntity;

import java.util.List;

public interface TypeService {

    List<TypeEntity> listByGroup(String typeGroup);
}
