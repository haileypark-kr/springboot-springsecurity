/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.study.springsecurity.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

	private final JwtProvider jwtProvider;

	@Value("${jwt.header}")
	private String AUTHORIZATION_HEADER;

	/**
	 * JWT 토큰의 인증 정보를 SecurityContext에 저장하는 역할.
	 * @param request
	 * @param response
	 * @param chain
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws
		IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String jwt = getToken(httpServletRequest);

		if (StringUtils.hasText(jwt) && jwtProvider.validateToken(jwt)) {
			Authentication authentication = jwtProvider.getAuthentication(jwt);
			SecurityContextHolder.getContext().setAuthentication(authentication);

			log.info("SecurityContext에 인증 정보를 저장했습니다 - {}, {}", authentication.getName(),
				((HttpServletRequest)request).getRequestURI());
		} else {
			log.error("JWT 정보가 없습니다. - {}", ((HttpServletRequest)request).getRequestURI());

		}

		chain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		String headerAuth = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(headerAuth) & headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
}
