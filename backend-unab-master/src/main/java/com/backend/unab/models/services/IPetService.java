package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.PetDto;

public interface IPetService {

	PetDto create(PetDto petDto);

	PetDto update(Long id, PetDto petDto);

	void delete(Long id);

	PetDto findById(Long id);

	List<PetDto> findAll();
}
