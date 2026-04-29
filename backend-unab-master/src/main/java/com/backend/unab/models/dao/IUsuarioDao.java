package com.backend.unab.models.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.User;

public interface IUsuarioDao extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);
}
