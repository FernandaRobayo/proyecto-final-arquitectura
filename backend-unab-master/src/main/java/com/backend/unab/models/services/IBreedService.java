package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.BreedDto;

public interface IBreedService {

	BreedDto create(BreedDto breedDto);

	BreedDto update(Long id, BreedDto breedDto);

	void delete(Long id);

	BreedDto findById(Long id);

	List<BreedDto> findAll();
}
