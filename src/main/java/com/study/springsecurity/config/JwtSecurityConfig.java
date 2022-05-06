/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.study.springsecurity.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.study.springsecurity.jwt.JwtFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	private final JwtFilter jwtFilter;

	@Override
	public void configure(HttpSecurity builder) throws Exception {
		builder.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
