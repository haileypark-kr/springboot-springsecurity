package com.study.springsecurity.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_user")
public class User {

	@Id
	@Column
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

	@Column
	private Boolean isUsed;

	@ManyToMany
	@JoinTable(
		name = "user_authority",
		joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
		inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "authority_id")}
	)
	private Set<Authority> authorities = new HashSet<>();

	public User() {
	}

	@Builder
	public User(String userId, String encryptedPassword, String name, String lastLogin, String role) {
		this.userId = userId;
		this.encryptedPassword = encryptedPassword;
		this.name = name;
		this.lastLogin = lastLogin;
		this.role = role;
		this.isUsed = true;
	}
}
