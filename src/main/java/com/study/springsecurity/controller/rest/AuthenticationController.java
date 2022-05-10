package com.study.springsecurity.controller.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.study.springsecurity.dto.LoginRequestDto;
import com.study.springsecurity.dto.TokenResponseDto;
import com.study.springsecurity.dto.UserDto;
import com.study.springsecurity.jwt.JwtFilter;
import com.study.springsecurity.jwt.JwtProvider;
import com.study.springsecurity.service.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class AuthenticationController {

	private final UserService accountService;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtProvider jwtProvider;

	@PostMapping("/register")
	public void register(UserDto registerDto) {

		this.accountService.createUserAccount(registerDto);
	}

	@PostMapping("/login")
	public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto requestDto) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
			requestDto.getUserName(),
			requestDto.getPassword());

		// UserDetailsService의 loadUserByUsername를 호출
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication); // 이거는 왜 여기서 또 저장? Jwt 필터에서 하자나
		// ==> /login은 permitAll이라서 필터 안탐

		String jwt = jwtProvider.generateJwt(authentication);

		HttpHeaders headers = new HttpHeaders();
		headers.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

		return new ResponseEntity<>(new TokenResponseDto(jwt), headers, HttpStatus.OK);
	}

	@PostMapping("/logout")
	public void logout() {

		System.out.println("logout clicked: ");
	}
}
