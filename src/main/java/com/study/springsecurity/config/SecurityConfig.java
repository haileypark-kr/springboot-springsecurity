package com.study.springsecurity.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.study.springsecurity.service.AccountService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final AccountService accountService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void configure(WebSecurity web) throws Exception {
		// static 경로 접근 허용
		web.ignoring()
			.requestMatchers(PathRequest.toStaticResources().atCommonLocations());
		// .antMatchers("/favicon.ico", "/error");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// spring-security의 userDetailService와 AccountService를 연동하고 passwordEncoder 설정
		auth.userDetailsService(accountService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable();

		http.authorizeRequests()
			.mvcMatchers("/login", "/register").permitAll()
			.anyRequest().authenticated()
		;

		http.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/index", true);

		http.logout()
			.logoutUrl("/logout")
			.logoutSuccessUrl("/");
	}
}
