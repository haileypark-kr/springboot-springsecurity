package com.study.springsecurity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_user")
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String userId;

	@Column
	private String encryptedPassword;

	@Column
	private String name;

	@Column
	private String lastLogin;

	@Column
	private String role;

	public Account() {
	}

	@Builder
	public Account(String userId, String encryptedPassword, String name, String lastLogin, String role) {
		this.userId = userId;
		this.encryptedPassword = encryptedPassword;
		this.name = name;
		this.lastLogin = lastLogin;
		this.role = role;
	}
}
