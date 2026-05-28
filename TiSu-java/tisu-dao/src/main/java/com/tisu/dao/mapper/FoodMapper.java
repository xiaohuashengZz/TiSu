package com.tisu.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tisu.model.entity.FoodEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FoodMapper extends BaseMapper<FoodEntity> {
}
