package com.backend.unab.dto;

import java.time.LocalDateTime;

public class AppointmentDto {

	private Long id;
	private Long petId;
	private String petName;
	private Long veterinarianId;
	private String veterinarianFullName;
	private LocalDateTime appointmentDateTime;
	private String reason;
	private String status;
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPetId() {
		return petId;
	}

	public void setPetId(Long petId) {
		this.petId = petId;
	}

	public String getPetName() {
		return petName;
	}

	public void setPetName(String petName) {
		this.petName = petName;
	}

	public Long getVeterinarianId() {
		return veterinarianId;
	}

	public void setVeterinarianId(Long veterinarianId) {
		this.veterinarianId = veterinarianId;
	}

	public String getVeterinarianFullName() {
		return veterinarianFullName;
	}

	public void setVeterinarianFullName(String veterinarianFullName) {
		this.veterinarianFullName = veterinarianFullName;
	}

	public LocalDateTime getAppointmentDateTime() {
		return appointmentDateTime;
	}

	public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
		this.appointmentDateTime = appointmentDateTime;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
}
