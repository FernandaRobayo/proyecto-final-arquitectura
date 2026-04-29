package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.TreatmentDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IMedicalRecordDao;
import com.backend.unab.models.dao.ITreatmentDao;
import com.backend.unab.models.entity.MedicalRecord;
import com.backend.unab.models.entity.Treatment;

@Service
public class TreatmentServiceImpl implements ITreatmentService {

	private final ITreatmentDao treatmentDao;
	private final IMedicalRecordDao medicalRecordDao;

	public TreatmentServiceImpl(ITreatmentDao treatmentDao, IMedicalRecordDao medicalRecordDao) {
		this.treatmentDao = treatmentDao;
		this.medicalRecordDao = medicalRecordDao;
	}

	@Override
	@Transactional
	public TreatmentDto create(TreatmentDto treatmentDto) {
		validate(treatmentDto);
		Treatment treatment = new Treatment();
		applyChanges(treatment, treatmentDto);
		return toDto(treatmentDao.save(treatment));
	}

	@Override
	@Transactional
	public TreatmentDto update(Long id, TreatmentDto treatmentDto) {
		validate(treatmentDto);
		Treatment treatment = findEntity(id);
		applyChanges(treatment, treatmentDto);
		return toDto(treatmentDao.save(treatment));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		treatmentDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public TreatmentDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<TreatmentDto> findAll() {
		return treatmentDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Treatment findEntity(Long id) {
		return treatmentDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Treatment with id " + id + " not found"));
	}

	private MedicalRecord findMedicalRecord(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Medical record id is required");
		}
		return medicalRecordDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Medical record with id " + id + " not found"));
	}

	private void validate(TreatmentDto treatmentDto) {
		if (treatmentDto == null) {
			throw new IllegalArgumentException("Treatment payload is required");
		}
		if (treatmentDto.getMedicalRecordId() == null) {
			throw new IllegalArgumentException("Medical record id is required");
		}
		if (!StringUtils.hasText(treatmentDto.getDescription())) {
			throw new IllegalArgumentException("Treatment description is required");
		}
	}

	private void applyChanges(Treatment treatment, TreatmentDto treatmentDto) {
		treatment.setMedicalRecord(findMedicalRecord(treatmentDto.getMedicalRecordId()));
		treatment.setDescription(treatmentDto.getDescription().trim());
		treatment.setMedication(treatmentDto.getMedication());
		treatment.setDosage(treatmentDto.getDosage());
		treatment.setDuration(treatmentDto.getDuration());
	}

	private TreatmentDto toDto(Treatment treatment) {
		TreatmentDto dto = new TreatmentDto();
		dto.setId(treatment.getId());
		dto.setMedicalRecordId(treatment.getMedicalRecord().getId());
		dto.setDescription(treatment.getDescription());
		dto.setMedication(treatment.getMedication());
		dto.setDosage(treatment.getDosage());
		dto.setDuration(treatment.getDuration());
		dto.setCreatedAt(treatment.getCreatedAt());
		return dto;
	}
}
