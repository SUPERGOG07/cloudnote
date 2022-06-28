package com.superdog.cloudnote.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.superdog.cloudnote.mapper.UserMapper;
import com.superdog.cloudnote.pojo.User;
import com.superdog.cloudnote.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
