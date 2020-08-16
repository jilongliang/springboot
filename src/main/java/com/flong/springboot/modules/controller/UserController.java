package com.flong.springboot.modules.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.flong.springboot.core.util.BuildConditionWrapper;
import com.flong.springboot.core.vo.Conditions;
import com.flong.springboot.modules.entity.User;
import com.flong.springboot.modules.mapper.UserMapper;
import com.flong.springboot.modules.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author:liangjl
 * @Date:2020-08-16
 * @Description:用户控制层
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * 添加
     */
    @PutMapping("/add")
    @ApiOperation(value = "添加用户",notes = "添加用户")
    public void add() {
        userMapper.insert(User.builder().userName("周伯通").passWord("123456").build());
    }

    /**
     * 修改
     * @param user
     */
    @PutMapping("/updateById")
    @ApiOperation(value = "修改用户",notes = "通过用户Id进行修改用户信息")
    public void updateById(@RequestBody User user) {
        userMapper.updateById(user);
    }
    /**
     * 删除通过多个主键Id进行删除
     * @param ids
     */
    @DeleteMapping("/deleteByIds")
    @ApiOperation(value = "修改删除",notes = "支持多个用户Id进行删除用户")
    public void deleteByIds(@RequestBody List<String> ids) {
        userMapper.deleteBatchIds(ids);
    }

    /**
     * 通过指定Id进行查询
     *
     * @param userId
     */
    @GetMapping("/getOne/{userId}")
    @ApiOperation(value = "查询一个用户信息",notes = "通过指定Id进行查询")
    public User getOne(@PathVariable("userId") Long userId) {
        User user = userMapper.selectById(userId);
        System.out.println(JSON.toJSON(user));
        return user ;
    }

    /**
     * 用户分页，参数有多个使用下标索引进行处理.如果有两个参数(如用户名和地址)：conditionList[0].fieldName=userName、 conditionList[0].fieldName=address
     * 未转码请求分页地址: http://localhost:7011/user/page?current=1&size=20&conditionList[0].fieldName=userName&conditionList[0].operation=LIKE&conditionList[0].value=周
     * 已转码请求分页地址: http://localhost:7011/user/page?current=1&size=20&conditionList[0].fieldName=userName&conditionList[0].operation=LIKE&conditionList[0].value=%E5%91%A8
     * @param page
     * @param conditions 条件
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "用户分页查询",notes = "通过多个条件进行查询用户信息")
    public IPage<User> page(Page page, Conditions conditions) {
        QueryWrapper<User> build = BuildConditionWrapper.build(conditions.getConditionList(), User.class);
        build.lambda().orderByDesc(User::getCreateTime);
        return userService.page(page, build);
    }


}
