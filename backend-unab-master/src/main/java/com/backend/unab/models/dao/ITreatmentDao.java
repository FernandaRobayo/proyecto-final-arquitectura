package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Treatment;

public interface ITreatmentDao extends JpaRepository<Treatment, Long> {
}
