package com.study.springsecurity.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.study.springsecurity.dto.UserRegisterDto;
import com.study.springsecurity.entity.Account;
import com.study.springsecurity.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
	private final AccountRepository repository;
	private final PasswordEncoder passwordEncoder;

	public void createUserAccount(UserRegisterDto userRegisterDto) {
		this.repository.save(
			Account.builder()
				.userId(userRegisterDto.getUserId())
				.name(userRegisterDto.getName())
				.encryptedPassword(passwordEncoder.encode(userRegisterDto.getPassword()))
				.role(userRegisterDto.getRole())
				// .lastLogin(DateUtils.getCurrentDate()) // 회원가입 시에는 last login 업데이트 안함
				.build());
	}

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		Optional<Account> userAccountOptional = repository.findByUserId(userId);
		if (!userAccountOptional.isPresent()) {
			throw new UsernameNotFoundException(userId);
		}

		Account userAccount = userAccountOptional.get();
		return User.builder()
			.username(userAccount.getName())
			.password(userAccount.getEncryptedPassword())
			.roles("ROLE")
			.build();

	}
}
