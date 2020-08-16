package com.flong.springboot.modules.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "测试管理")
@RestController
public class SimpleController {

    @Value("${runEvn}")
    private String runEvn;

    @GetMapping("/test")
    @ApiOperation(value = "简单接口测试",notes = "简单接口测试")
    public String test() {
        return "this spring boot " + runEvn + " date long " + System.currentTimeMillis();
    }

}
