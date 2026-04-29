package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Species;

public interface ISpeciesDao extends JpaRepository<Species, Long> {
}
