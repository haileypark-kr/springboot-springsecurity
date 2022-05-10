package com.study.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.study.springsecurity.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	// @Query("select u from User as u where u.userId=?1")
	// Optional<User> findByUserId(String userId);

	// EntityGraph 사용하면 해당 필드를 LAZY 조회가 아니라 EAGER 조회로 가져옴

	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByUserId(String userId);
}
