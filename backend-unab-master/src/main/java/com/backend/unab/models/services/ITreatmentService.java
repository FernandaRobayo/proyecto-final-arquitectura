package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.TreatmentDto;

public interface ITreatmentService {

	TreatmentDto create(TreatmentDto treatmentDto);

	TreatmentDto update(Long id, TreatmentDto treatmentDto);

	void delete(Long id);

	TreatmentDto findById(Long id);

	List<TreatmentDto> findAll();
}
