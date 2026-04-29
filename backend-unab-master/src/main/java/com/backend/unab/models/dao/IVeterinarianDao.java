package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Veterinarian;

public interface IVeterinarianDao extends JpaRepository<Veterinarian, Long> {
}
