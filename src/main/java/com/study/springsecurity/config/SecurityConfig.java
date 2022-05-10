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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.study.springsecurity.jwt.JwtAccessDeniedHandler;
import com.study.springsecurity.jwt.JwtAuthenticationEntryPoint;
import com.study.springsecurity.jwt.JwtFilter;
import com.study.springsecurity.jwt.JwtProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true) // 메소드 단위 @PreAuthorized 어노테이션 사용을 위해 추가
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtProvider jwtProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
	private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
			.antMatchers(
				"/h2-console/**"
				, "/favicon.ico"
				, "/error"
				, "/api/login"
				, "/api/signup"
			);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			// token을 사용하는 방식이기 때문에 csrf를 disable합니다.
			.csrf().disable()

			.exceptionHandling()
			.authenticationEntryPoint(jwtAuthenticationEntryPoint)
			.accessDeniedHandler(jwtAccessDeniedHandler)

			// enable h2-console
			// X-Frame-Options 을 통해 브라우저가 <frame>, <iframe> 혹은 <object> 태그를 렌더링 해야하는지 막아야하는지를 알려준다.
			// 아래 설정은 same origin에 해당하는 frame 내에서 표시를 허용한다.
			.and()
			.headers()
			.frameOptions()
			.sameOrigin()

			// 세션을 사용하지 않기 때문에 STATELESS로 설정
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 인증 시 세션 사용 X

			.and()
			.authorizeRequests()
			// .antMatchers("/api/signup").permitAll() // 회원가입
			// .antMatchers("/api/login").permitAll() // 로그인 API
			.anyRequest().authenticated()

			.and()
			.addFilterBefore(new JwtFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class);

	}
}
