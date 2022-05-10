package com.study.springsecurity.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class UserDto {
	private String userId;
	private String password;
	private String name;
	private String role;

	public UserDto() {
	}

	@Builder
	public UserDto(String userId, String password, String name, String role) {
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.role = role;
	}
}
