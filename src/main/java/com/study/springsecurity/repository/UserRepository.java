package com.study.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.study.springsecurity.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select u from User as u where u.userId=?1")
	Optional<User> findByUserId(String userId);
}
