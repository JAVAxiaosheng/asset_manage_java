package com.graduation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduation.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
