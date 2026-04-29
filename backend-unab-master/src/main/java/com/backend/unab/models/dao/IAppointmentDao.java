package com.backend.unab.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.unab.models.entity.Appointment;

public interface IAppointmentDao extends JpaRepository<Appointment, Long> {
}
