package com.study.springsecurity.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v0.1/test")
@Slf4j
public class TestController {

	@GetMapping("/general")
	public String general() {
		log.info("general");
		return "Success - general";
	}

	@GetMapping("/admin")
	public String admin() {
		log.info("admin");
		return "Success - admin";
	}
}
