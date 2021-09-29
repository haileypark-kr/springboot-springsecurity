package com.study.springsecurity.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserWebController {

	@GetMapping("/register")
	public String getRegisterForm() {
		return "register";
	}

	@GetMapping("/login")
	public String getloginForm() {
		return "login";
	}
}
