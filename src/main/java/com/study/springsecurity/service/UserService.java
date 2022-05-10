package com.study.springsecurity.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.springsecurity.dto.UserDto;
import com.study.springsecurity.entity.User;
import com.study.springsecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security에서 가장 중요한 부분: UserDetailsService implement한 UserService 구현.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	private final UserRepository repository;

	@Qualifier(value = "customPasswordEncoder")
	private final PasswordEncoder passwordEncoder;

	public void createUserAccount(UserDto userRegisterDto) {
		this.repository.save(
			User.builder()
				.name(userRegisterDto.getName())
				.encryptedPassword(passwordEncoder.encode(userRegisterDto.getPassword()))
				.role(userRegisterDto.getRole())
				// .lastLogin(DateUtils.getCurrentDate()) // 회원가입 시에는 last login 업데이트 안함
				.build());
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

		return repository.findOneWithAuthoritiesByUserId(userId)
			.map(u -> convert(u, userId))
			.orElseThrow(() -> new UsernameNotFoundException(userId + "를 찾을 수 없습니다."));

	}

	private org.springframework.security.core.userdetails.User convert(User user, String userId) {
		if (!user.getIsUsed()) {
			throw new RuntimeException(userId + "는 삭제된 사용자입니다.");
		}
		List<SimpleGrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
			.map(authority -> new SimpleGrantedAuthority(authority.getName()))
			.collect(Collectors.toList());

		return new org.springframework.security.core.userdetails.User(userId,
			user.getEncryptedPassword(),
			grantedAuthorities);
	}
}
