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

import com.backend.unab.dto.TreatmentDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IMedicalRecordDao;
import com.backend.unab.models.dao.ITreatmentDao;
import com.backend.unab.models.entity.Appointment;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Customer;
import com.backend.unab.models.entity.MedicalRecord;
import com.backend.unab.models.entity.Pet;
import com.backend.unab.models.entity.Species;
import com.backend.unab.models.entity.Treatment;
import com.backend.unab.models.entity.Veterinarian;

@ExtendWith(MockitoExtension.class)
class TreatmentServiceImplTest {

	@Mock
	private ITreatmentDao treatmentDao;

	@Mock
	private IMedicalRecordDao medicalRecordDao;

	@InjectMocks
	private TreatmentServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		TreatmentDto payload = buildTreatmentDto();
		when(medicalRecordDao.findById(10L)).thenReturn(Optional.of(buildMedicalRecordEntity(10L)));
		when(treatmentDao.save(any(Treatment.class))).thenAnswer(invocation -> {
			Treatment treatment = invocation.getArgument(0);
			treatment.setId(1L);
			treatment.setCreatedAt(LocalDateTime.now());
			return treatment;
		});

		TreatmentDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Anti-inflammatory care", result.getDescription());
		verify(medicalRecordDao).findById(10L);
		verify(treatmentDao).save(any(Treatment.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		TreatmentDto payload = new TreatmentDto();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(payload));

		assertEquals("Medical record id is required", exception.getMessage());
		verify(treatmentDao, never()).save(any(Treatment.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Treatment entity = buildTreatmentEntity(1L);
		TreatmentDto payload = buildTreatmentDto();
		payload.setMedication("Carprofen");

		when(treatmentDao.findById(1L)).thenReturn(Optional.of(entity));
		when(medicalRecordDao.findById(10L)).thenReturn(Optional.of(buildMedicalRecordEntity(10L)));
		when(treatmentDao.save(any(Treatment.class))).thenAnswer(invocation -> invocation.getArgument(0));

		TreatmentDto result = service.update(1L, payload);

		assertEquals("Carprofen", result.getMedication());
		verify(treatmentDao).findById(1L);
		verify(medicalRecordDao).findById(10L);
		verify(treatmentDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(treatmentDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildTreatmentDto()));

		assertEquals("Treatment with id 99 not found", exception.getMessage());
		verify(treatmentDao).findById(99L);
		verify(treatmentDao, never()).save(any(Treatment.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Treatment entity = buildTreatmentEntity(1L);
		when(treatmentDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(treatmentDao).findById(1L);
		verify(treatmentDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(treatmentDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Treatment with id 99 not found", exception.getMessage());
		verify(treatmentDao).findById(99L);
		verify(treatmentDao, never()).delete(any(Treatment.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(treatmentDao.findById(1L)).thenReturn(Optional.of(buildTreatmentEntity(1L)));

		TreatmentDto result = service.findById(1L);

		assertEquals("Anti-inflammatory care", result.getDescription());
		verify(treatmentDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(treatmentDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Treatment with id 99 not found", exception.getMessage());
		verify(treatmentDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(treatmentDao.findAll()).thenReturn(List.of(buildTreatmentEntity(1L), buildTreatmentEntity(2L)));

		List<TreatmentDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(treatmentDao).findAll();
		verifyNoMoreInteractions(treatmentDao);
	}

	private TreatmentDto buildTreatmentDto() {
		TreatmentDto dto = new TreatmentDto();
		dto.setMedicalRecordId(10L);
		dto.setDescription("Anti-inflammatory care");
		dto.setMedication("Meloxicam");
		dto.setDosage("1 tablet daily");
		dto.setDuration("5 days");
		return dto;
	}

	private Treatment buildTreatmentEntity(Long id) {
		Treatment treatment = new Treatment();
		treatment.setId(id);
		treatment.setMedicalRecord(buildMedicalRecordEntity(10L));
		treatment.setDescription("Anti-inflammatory care");
		treatment.setMedication("Meloxicam");
		treatment.setDosage("1 tablet daily");
		treatment.setDuration("5 days");
		treatment.setCreatedAt(LocalDateTime.now());
		return treatment;
	}

	private MedicalRecord buildMedicalRecordEntity(Long id) {
		MedicalRecord medicalRecord = new MedicalRecord();
		medicalRecord.setId(id);
		medicalRecord.setAppointment(buildAppointmentEntity(20L));
		medicalRecord.setDiagnosis("Healthy");
		medicalRecord.setNotes("No issues");
		medicalRecord.setWeight(new BigDecimal("12.50"));
		medicalRecord.setTemperature(new BigDecimal("38.5"));
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
