package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.AppointmentDto;

public interface IAppointmentService {

	AppointmentDto create(AppointmentDto appointmentDto);

	AppointmentDto update(Long id, AppointmentDto appointmentDto);

	void delete(Long id);

	AppointmentDto findById(Long id);

	List<AppointmentDto> findAll();
}
