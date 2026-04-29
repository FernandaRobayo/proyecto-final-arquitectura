package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.VeterinarianDto;

public interface IVeterinarianService {

	VeterinarianDto create(VeterinarianDto veterinarianDto);

	VeterinarianDto update(Long id, VeterinarianDto veterinarianDto);

	void delete(Long id);

	VeterinarianDto findById(Long id);

	List<VeterinarianDto> findAll();
}
