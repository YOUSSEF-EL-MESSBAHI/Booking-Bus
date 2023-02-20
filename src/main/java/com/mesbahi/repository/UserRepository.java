package com.mesbahi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mesbahi.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	
	User findByEmail(String emailId);
}
