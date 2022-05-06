/******************************************************************************************
 * 본 프로그램소스는 하나은행의 사전승인 없이 임의복제, 복사, 배포할 수 없음
 *
 * Copyright (C) 2018 by co.,Ltd. All right reserved.
 ******************************************************************************************/
package com.study.springsecurity.jwt;

import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtProvider implements InitializingBean {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${jwt.token-validity-in-seconds}")
	private int jwtExpirationMs;

	private static final String AUTHORITIES_KEY = "auth";

	private Key key;

	@Override
	public void afterPropertiesSet() throws Exception {
		// NOTE: key는 base64 encoding 필요 (어래 라인). 그러나 생성된 secret key 가 openssl rand -base64이므로 생략
		// byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);

		key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
	}

	public String generateJwt(Authentication authentication) {
		String authorities = authentication.getAuthorities()
			.stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder()
			.setSubject(authentication.getName())
			.claim(AUTHORITIES_KEY, authorities)
			.signWith(key, SignatureAlgorithm.HS512)
			.setExpiration(expiryDate)
			.compact();

	}

	public Authentication getAuthentication(String token) {
		Claims claims = Jwts
			.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token)
			.getBody();

		List<SimpleGrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);
		return new UsernamePasswordAuthenticationToken(principal, token, authorities);

	}

	// Jwt 토큰 유효성 검사
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SignatureException ex) {
			log.error("Invalid JWT signature");
		} catch (MalformedJwtException ex) {
			log.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			log.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			log.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			log.error("JWT claims string is empty.");
		}
		return false;
	}

}
