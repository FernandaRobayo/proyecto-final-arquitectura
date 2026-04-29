package com.backend.unab.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Role;

public interface IRoleDao extends JpaRepository<Role, Long> {

	Optional<Role> findByName(String name);
}
