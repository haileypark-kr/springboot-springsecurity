package com.study.springsecurity.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserRegisterDto {
	private String userId;
	private String password;
	private String name;
	private String role;

	public UserRegisterDto() {
	}

	@Builder
	public UserRegisterDto(String userId, String password, String name, String role) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.role = role;
	}
}
