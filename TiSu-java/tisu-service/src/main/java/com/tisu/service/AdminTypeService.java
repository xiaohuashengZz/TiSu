package com.tisu.service;

import com.tisu.model.dto.TypeDTO;
import com.tisu.model.entity.TypeEntity;

import java.util.List;

public interface AdminTypeService {

    List<TypeEntity> listTypes(String typeGroup);

    void createType(TypeDTO dto);

    void updateType(Long id, TypeDTO dto);

    void deleteType(Long id);
}
