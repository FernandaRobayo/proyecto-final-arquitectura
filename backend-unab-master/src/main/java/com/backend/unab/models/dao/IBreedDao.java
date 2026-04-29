package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Breed;

public interface IBreedDao extends JpaRepository<Breed, Long> {
}
