package com.superdog.cloudnote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.superdog.cloudnote.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
