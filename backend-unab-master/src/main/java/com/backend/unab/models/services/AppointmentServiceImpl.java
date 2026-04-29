package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.AppointmentDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IAppointmentDao;
import com.backend.unab.models.dao.IPetDao;
import com.backend.unab.models.dao.IVeterinarianDao;
import com.backend.unab.models.entity.Appointment;
import com.backend.unab.models.entity.Pet;
import com.backend.unab.models.entity.Veterinarian;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

	private final IAppointmentDao appointmentDao;
	private final IPetDao petDao;
	private final IVeterinarianDao veterinarianDao;

	public AppointmentServiceImpl(IAppointmentDao appointmentDao, IPetDao petDao, IVeterinarianDao veterinarianDao) {
		this.appointmentDao = appointmentDao;
		this.petDao = petDao;
		this.veterinarianDao = veterinarianDao;
	}

	@Override
	@Transactional
	public AppointmentDto create(AppointmentDto appointmentDto) {
		validate(appointmentDto);
		Appointment appointment = new Appointment();
		applyChanges(appointment, appointmentDto);
		return toDto(appointmentDao.save(appointment));
	}

	@Override
	@Transactional
	public AppointmentDto update(Long id, AppointmentDto appointmentDto) {
		validate(appointmentDto);
		Appointment appointment = findEntity(id);
		applyChanges(appointment, appointmentDto);
		return toDto(appointmentDao.save(appointment));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		appointmentDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public AppointmentDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<AppointmentDto> findAll() {
		return appointmentDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Appointment findEntity(Long id) {
		return appointmentDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " not found"));
	}

	private Pet findPet(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Pet id is required");
		}
		return petDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet with id " + id + " not found"));
	}

	private Veterinarian findVeterinarian(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Veterinarian id is required");
		}
		return veterinarianDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Veterinarian with id " + id + " not found"));
	}

	private void validate(AppointmentDto appointmentDto) {
		if (appointmentDto == null) {
			throw new IllegalArgumentException("Appointment payload is required");
		}
		if (appointmentDto.getPetId() == null) {
			throw new IllegalArgumentException("Pet id is required");
		}
		if (appointmentDto.getVeterinarianId() == null) {
			throw new IllegalArgumentException("Veterinarian id is required");
		}
		if (appointmentDto.getAppointmentDateTime() == null) {
			throw new IllegalArgumentException("Appointment date time is required");
		}
		if (!StringUtils.hasText(appointmentDto.getReason())) {
			throw new IllegalArgumentException("Appointment reason is required");
		}
		if (!StringUtils.hasText(appointmentDto.getStatus())) {
			throw new IllegalArgumentException("Appointment status is required");
		}
	}

	private void applyChanges(Appointment appointment, AppointmentDto appointmentDto) {
		appointment.setPet(findPet(appointmentDto.getPetId()));
		appointment.setVeterinarian(findVeterinarian(appointmentDto.getVeterinarianId()));
		appointment.setAppointmentDateTime(appointmentDto.getAppointmentDateTime());
		appointment.setReason(appointmentDto.getReason().trim());
		appointment.setStatus(appointmentDto.getStatus().trim());
	}

	private AppointmentDto toDto(Appointment appointment) {
		AppointmentDto dto = new AppointmentDto();
		dto.setId(appointment.getId());
		dto.setPetId(appointment.getPet().getId());
		dto.setPetName(appointment.getPet().getName());
		dto.setVeterinarianId(appointment.getVeterinarian().getId());
		dto.setVeterinarianFullName(appointment.getVeterinarian().getFullName());
		dto.setAppointmentDateTime(appointment.getAppointmentDateTime());
		dto.setReason(appointment.getReason());
		dto.setStatus(appointment.getStatus());
		dto.setCreatedAt(appointment.getCreatedAt());
		return dto;
	}
}
