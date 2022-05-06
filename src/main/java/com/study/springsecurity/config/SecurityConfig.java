package com.study.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.study.springsecurity.jwt.JwtAccessDeniedHandler;
import com.study.springsecurity.jwt.JwtAuthenticationEntryPoint;
import com.study.springsecurity.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메소드 단위 @PreAuthorized 어노테이션 사용을 위해 추가
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtSecurityConfig jwtSecurityConfig;
	private final JwtProvider jwtProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public BCryptPasswordEncoder customPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers("/h2-console/**"
				, "/favicon.ico"
			);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)

			.and()
			.headers()
			.frameOptions()
			.sameOrigin()

			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 인증 시 세션 사용 X

			.and()
			.authorizeRequests()
			.antMatchers("/api/signup").permitAll() // 회원가입
			.antMatchers("/api/authenticate").permitAll() // 토큰을 받기 위한 API
			.anyRequest().authenticated()

			.and()
			.apply(jwtSecurityConfig); // JWT Filter가 적용된 JWT SecurityConfig 추가

	}
}
