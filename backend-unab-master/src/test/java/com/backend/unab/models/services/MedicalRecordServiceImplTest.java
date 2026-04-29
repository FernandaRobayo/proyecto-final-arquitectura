package com.backend.unab.models.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.unab.dto.MedicalRecordDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IAppointmentDao;
import com.backend.unab.models.dao.IMedicalRecordDao;
import com.backend.unab.models.entity.Appointment;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Customer;
import com.backend.unab.models.entity.MedicalRecord;
import com.backend.unab.models.entity.Pet;
import com.backend.unab.models.entity.Species;
import com.backend.unab.models.entity.Veterinarian;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceImplTest {

	@Mock
	private IMedicalRecordDao medicalRecordDao;

	@Mock
	private IAppointmentDao appointmentDao;

	@InjectMocks
	private MedicalRecordServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		MedicalRecordDto payload = buildMedicalRecordDto();
		when(appointmentDao.findById(10L)).thenReturn(Optional.of(buildAppointmentEntity(10L)));
		when(medicalRecordDao.save(any(MedicalRecord.class))).thenAnswer(invocation -> {
			MedicalRecord medicalRecord = invocation.getArgument(0);
			medicalRecord.setId(1L);
			medicalRecord.setCreatedAt(LocalDateTime.now());
			return medicalRecord;
		});

		MedicalRecordDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Healthy", result.getDiagnosis());
		verify(appointmentDao).findById(10L);
		verify(medicalRecordDao).save(any(MedicalRecord.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		MedicalRecordDto payload = new MedicalRecordDto();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(payload));

		assertEquals("Appointment id is required", exception.getMessage());
		verify(medicalRecordDao, never()).save(any(MedicalRecord.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		MedicalRecord entity = buildMedicalRecordEntity(1L);
		MedicalRecordDto payload = buildMedicalRecordDto();
		payload.setDiagnosis("Requires follow-up");

		when(medicalRecordDao.findById(1L)).thenReturn(Optional.of(entity));
		when(appointmentDao.findById(10L)).thenReturn(Optional.of(buildAppointmentEntity(10L)));
		when(medicalRecordDao.save(any(MedicalRecord.class))).thenAnswer(invocation -> invocation.getArgument(0));

		MedicalRecordDto result = service.update(1L, payload);

		assertEquals("Requires follow-up", result.getDiagnosis());
		verify(medicalRecordDao).findById(1L);
		verify(appointmentDao).findById(10L);
		verify(medicalRecordDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(medicalRecordDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildMedicalRecordDto()));

		assertEquals("Medical record with id 99 not found", exception.getMessage());
		verify(medicalRecordDao).findById(99L);
		verify(medicalRecordDao, never()).save(any(MedicalRecord.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		MedicalRecord entity = buildMedicalRecordEntity(1L);
		when(medicalRecordDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(medicalRecordDao).findById(1L);
		verify(medicalRecordDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(medicalRecordDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Medical record with id 99 not found", exception.getMessage());
		verify(medicalRecordDao).findById(99L);
		verify(medicalRecordDao, never()).delete(any(MedicalRecord.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(medicalRecordDao.findById(1L)).thenReturn(Optional.of(buildMedicalRecordEntity(1L)));

		MedicalRecordDto result = service.findById(1L);

		assertEquals("Healthy", result.getDiagnosis());
		verify(medicalRecordDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(medicalRecordDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Medical record with id 99 not found", exception.getMessage());
		verify(medicalRecordDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(medicalRecordDao.findAll()).thenReturn(List.of(buildMedicalRecordEntity(1L), buildMedicalRecordEntity(2L)));

		List<MedicalRecordDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(medicalRecordDao).findAll();
		verifyNoMoreInteractions(medicalRecordDao);
	}

	private MedicalRecordDto buildMedicalRecordDto() {
		MedicalRecordDto dto = new MedicalRecordDto();
		dto.setAppointmentId(10L);
		dto.setDiagnosis("Healthy");
		dto.setNotes("No issues");
		dto.setWeight(new BigDecimal("12.50"));
		dto.setTemperature(new BigDecimal("38.5"));
		return dto;
	}

	private MedicalRecord buildMedicalRecordEntity(Long id) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setId(id);
		medicalRecord.setAppointment(buildAppointmentEntity(10L));
		medicalRecord.setDiagnosis("Healthy");
		medicalRecord.setNotes("No issues");
		medicalRecord.setWeight(new BigDecimal("12.50"));
		medicalRecord.setTemperature(new BigDecimal("38.5"));
		medicalRecord.setCreatedAt(LocalDateTime.now());
		return medicalRecord;
	}

	private Appointment buildAppointmentEntity(Long id) {
		Customer customer = new Customer();
		customer.setId(1L);
		customer.setFullName("John Doe");

		Species species = new Species();
		species.setId(30L);
		species.setName("Canine");

		Breed breed = new Breed();
		breed.setId(40L);
		breed.setName("Labrador");
		breed.setSpecies(species);

		Pet pet = new Pet();
		pet.setId(50L);
		pet.setCustomer(customer);
		pet.setBreed(breed);
		pet.setName("Milo");
		pet.setBirthDate(LocalDate.of(2021, 5, 10));

		Veterinarian veterinarian = new Veterinarian();
		veterinarian.setId(60L);
		veterinarian.setFullName("Dr. Smith");

		Appointment appointment = new Appointment();
		appointment.setId(id);
		appointment.setPet(pet);
		appointment.setVeterinarian(veterinarian);
		appointment.setAppointmentDateTime(LocalDateTime.of(2026, 4, 28, 10, 0));
		appointment.setReason("Checkup");
		appointment.setStatus("Scheduled");
		return appointment;
	}
}
