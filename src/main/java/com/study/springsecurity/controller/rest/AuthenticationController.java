package com.study.springsecurity.controller.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springsecurity.dto.UserRegisterDto;
import com.study.springsecurity.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final UserService accountService;

	@PostMapping("/register")
	public void register(UserRegisterDto registerDto) {

		this.accountService.createUserAccount(registerDto);
	}

	@PostMapping("/logout")
	public void logout() {

		System.out.println("logout clicked: ");
	}
}
