/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.study.springsecurity.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * @title 유효한 자격증명을 제공하지 않고 접근하려 할 때 401 Unauthorized 에러 리턴
 * @since
 * @author soohyun
 * @see <pre></pre>
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) throws IOException, ServletException {

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}
