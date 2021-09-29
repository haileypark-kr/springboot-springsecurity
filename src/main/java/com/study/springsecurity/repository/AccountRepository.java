package com.study.springsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.study.springsecurity.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select u from Account as u where u.userId=?1")
	Optional<Account> findByUserId(String userId);
}
