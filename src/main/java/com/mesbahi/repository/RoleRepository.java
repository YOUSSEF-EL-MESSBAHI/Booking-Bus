package com.mesbahi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mesbahi.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {

	Role findByRole(String name);
}
