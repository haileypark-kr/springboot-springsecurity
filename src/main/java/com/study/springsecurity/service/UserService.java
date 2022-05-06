package com.study.springsecurity.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.springsecurity.dto.UserRegisterDto;
import com.study.springsecurity.entity.User;
import com.study.springsecurity.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;

	public void createUserAccount(UserRegisterDto userRegisterDto) {
		this.repository.save(
			User.builder()
				.name(userRegisterDto.getName())
				.encryptedPassword(passwordEncoder.encode(userRegisterDto.getPassword()))
				// .role(userRegisterDto.getRole())
				// .lastLogin(DateUtils.getCurrentDate()) // 회원가입 시에는 last login 업데이트 안함
				.build());
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<User> userAccountOptional = repository.findByUserId(userId);
		if (!userAccountOptional.isPresent()) {
			throw new UsernameNotFoundException(userId);
		}

		User userAccount = userAccountOptional.get();
		return org.springframework.security.core.userdetails.User.builder()
			.username(userAccount.getName())
			.password(userAccount.getEncryptedPassword())
			.roles("ROLE")
			.build();

	}
}
