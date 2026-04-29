package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Pet;

public interface IPetDao extends JpaRepository<Pet, Long> {
}
