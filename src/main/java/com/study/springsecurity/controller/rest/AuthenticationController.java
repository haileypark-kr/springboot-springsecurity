package com.study.springsecurity.controller.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springsecurity.dto.UserRegisterDto;
import com.study.springsecurity.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

	private final AccountService accountService;

	@PostMapping("/register")
	public void register(UserRegisterDto registerDto) {

		this.accountService.createUserAccount(registerDto);
	}

	@PostMapping("/logout")
	public void logout() {

		System.out.println("logout clicked: ");
	}
}
