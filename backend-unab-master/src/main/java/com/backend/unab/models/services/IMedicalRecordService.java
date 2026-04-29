package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.MedicalRecordDto;

public interface IMedicalRecordService {

	MedicalRecordDto create(MedicalRecordDto medicalRecordDto);

	MedicalRecordDto update(Long id, MedicalRecordDto medicalRecordDto);

	void delete(Long id);

	MedicalRecordDto findById(Long id);

	List<MedicalRecordDto> findAll();
}
