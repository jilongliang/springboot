package com.flong.springboot.modules.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.flong.springboot.modules.entity.User;
import com.flong.springboot.modules.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

}
