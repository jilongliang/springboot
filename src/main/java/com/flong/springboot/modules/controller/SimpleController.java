package com.flong.springboot.modules.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SimpleController {

	@Value("${runEvn}")
	private String runEvn;
	
	@GetMapping("/test")
	public String test() {
		return "this spring boot " + runEvn +" date long " + System.currentTimeMillis();
	}

}
