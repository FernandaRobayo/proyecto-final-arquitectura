package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.MedicalRecordDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IAppointmentDao;
import com.backend.unab.models.dao.IMedicalRecordDao;
import com.backend.unab.models.entity.Appointment;
import com.backend.unab.models.entity.MedicalRecord;

@Service
public class MedicalRecordServiceImpl implements IMedicalRecordService {

	private final IMedicalRecordDao medicalRecordDao;
	private final IAppointmentDao appointmentDao;

	public MedicalRecordServiceImpl(IMedicalRecordDao medicalRecordDao, IAppointmentDao appointmentDao) {
		this.medicalRecordDao = medicalRecordDao;
		this.appointmentDao = appointmentDao;
	}

	@Override
	@Transactional
	public MedicalRecordDto create(MedicalRecordDto medicalRecordDto) {
		validate(medicalRecordDto);
		MedicalRecord medicalRecord = new MedicalRecord();
		applyChanges(medicalRecord, medicalRecordDto);
		return toDto(medicalRecordDao.save(medicalRecord));
	}

	@Override
	@Transactional
	public MedicalRecordDto update(Long id, MedicalRecordDto medicalRecordDto) {
		validate(medicalRecordDto);
		MedicalRecord medicalRecord = findEntity(id);
		applyChanges(medicalRecord, medicalRecordDto);
		return toDto(medicalRecordDao.save(medicalRecord));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		medicalRecordDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public MedicalRecordDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<MedicalRecordDto> findAll() {
		return medicalRecordDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private MedicalRecord findEntity(Long id) {
		return medicalRecordDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Medical record with id " + id + " not found"));
	}

	private Appointment findAppointment(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Appointment id is required");
		}
		return appointmentDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " not found"));
	}

	private void validate(MedicalRecordDto medicalRecordDto) {
		if (medicalRecordDto == null) {
			throw new IllegalArgumentException("Medical record payload is required");
		}
		if (medicalRecordDto.getAppointmentId() == null) {
			throw new IllegalArgumentException("Appointment id is required");
		}
		if (!StringUtils.hasText(medicalRecordDto.getDiagnosis())) {
			throw new IllegalArgumentException("Diagnosis is required");
		}
	}

	private void applyChanges(MedicalRecord medicalRecord, MedicalRecordDto medicalRecordDto) {
		medicalRecord.setAppointment(findAppointment(medicalRecordDto.getAppointmentId()));
		medicalRecord.setDiagnosis(medicalRecordDto.getDiagnosis().trim());
		medicalRecord.setNotes(medicalRecordDto.getNotes());
		medicalRecord.setWeight(medicalRecordDto.getWeight());
		medicalRecord.setTemperature(medicalRecordDto.getTemperature());
	}

	private MedicalRecordDto toDto(MedicalRecord medicalRecord) {
		MedicalRecordDto dto = new MedicalRecordDto();
		dto.setId(medicalRecord.getId());
		dto.setAppointmentId(medicalRecord.getAppointment().getId());
		dto.setDiagnosis(medicalRecord.getDiagnosis());
		dto.setNotes(medicalRecord.getNotes());
		dto.setWeight(medicalRecord.getWeight());
		dto.setTemperature(medicalRecord.getTemperature());
		dto.setCreatedAt(medicalRecord.getCreatedAt());
		return dto;
	}
}
