package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.SpeciesDto;

public interface ISpeciesService {

	SpeciesDto create(SpeciesDto speciesDto);

	SpeciesDto update(Long id, SpeciesDto speciesDto);

	void delete(Long id);

	SpeciesDto findById(Long id);

	List<SpeciesDto> findAll();
}
