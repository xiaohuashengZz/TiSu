package com.tisu.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tisu.model.entity.UserProfileEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserProfileMapper extends BaseMapper<UserProfileEntity> {
}
