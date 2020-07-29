package com.flong.springboot.modules.controller;


import com.alibaba.fastjson.JSON;
import com.flong.springboot.modules.entity.User;
import com.flong.springboot.modules.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest")
public class UserController {

    @Autowired
    UserMapper userMapper;

    /**
     * 添加
     */
    @RequestMapping("/add")
    public void add() {
        userMapper.insert(User.builder().userName("周伯通").passWord("123456").build());
    }

    /**
     * 通过指定Id进行查询
     * @param userId
     */
    @GetMapping("/get/{userId}")
    public void get(@PathVariable("userId") Long userId) {
        User user = userMapper.selectById(userId);
        System.out.println(JSON.toJSON(user));

    }


}
