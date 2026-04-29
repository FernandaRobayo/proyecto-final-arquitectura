package com.backend.unab.models.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.unab.dto.AppointmentDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IAppointmentDao;
import com.backend.unab.models.dao.IPetDao;
import com.backend.unab.models.dao.IVeterinarianDao;
import com.backend.unab.models.entity.Appointment;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Customer;
import com.backend.unab.models.entity.Pet;
import com.backend.unab.models.entity.Species;
import com.backend.unab.models.entity.Veterinarian;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

	@Mock
	private IAppointmentDao appointmentDao;

	@Mock
	private IPetDao petDao;

	@Mock
	private IVeterinarianDao veterinarianDao;

	@InjectMocks
	private AppointmentServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		AppointmentDto payload = buildAppointmentDto();
		when(petDao.findById(10L)).thenReturn(Optional.of(buildPetEntity(10L)));
		when(veterinarianDao.findById(20L)).thenReturn(Optional.of(buildVeterinarianEntity(20L)));
		when(appointmentDao.save(any(Appointment.class))).thenAnswer(invocation -> {
			Appointment appointment = invocation.getArgument(0);
			appointment.setId(1L);
			appointment.setCreatedAt(LocalDateTime.now());
			return appointment;
		});

		AppointmentDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Milo", result.getPetName());
		assertEquals("Dr. Smith", result.getVeterinarianFullName());
		verify(petDao).findById(10L);
		verify(veterinarianDao).findById(20L);
		verify(appointmentDao).save(any(Appointment.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		AppointmentDto payload = new AppointmentDto();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(payload));

		assertEquals("Pet id is required", exception.getMessage());
		verify(appointmentDao, never()).save(any(Appointment.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Appointment entity = buildAppointmentEntity(1L);
		AppointmentDto payload = buildAppointmentDto();
		payload.setStatus("Completed");

		when(appointmentDao.findById(1L)).thenReturn(Optional.of(entity));
		when(petDao.findById(10L)).thenReturn(Optional.of(buildPetEntity(10L)));
		when(veterinarianDao.findById(20L)).thenReturn(Optional.of(buildVeterinarianEntity(20L)));
		when(appointmentDao.save(any(Appointment.class))).thenAnswer(invocation -> invocation.getArgument(0));

		AppointmentDto result = service.update(1L, payload);

		assertEquals("Completed", result.getStatus());
		verify(appointmentDao).findById(1L);
		verify(petDao).findById(10L);
		verify(veterinarianDao).findById(20L);
		verify(appointmentDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(appointmentDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildAppointmentDto()));

		assertEquals("Appointment with id 99 not found", exception.getMessage());
		verify(appointmentDao).findById(99L);
		verify(appointmentDao, never()).save(any(Appointment.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Appointment entity = buildAppointmentEntity(1L);
		when(appointmentDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(appointmentDao).findById(1L);
		verify(appointmentDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(appointmentDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Appointment with id 99 not found", exception.getMessage());
		verify(appointmentDao).findById(99L);
		verify(appointmentDao, never()).delete(any(Appointment.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(appointmentDao.findById(1L)).thenReturn(Optional.of(buildAppointmentEntity(1L)));

		AppointmentDto result = service.findById(1L);

		assertEquals("Checkup", result.getReason());
		verify(appointmentDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(appointmentDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Appointment with id 99 not found", exception.getMessage());
		verify(appointmentDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(appointmentDao.findAll()).thenReturn(List.of(buildAppointmentEntity(1L), buildAppointmentEntity(2L)));

		List<AppointmentDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(appointmentDao).findAll();
		verifyNoMoreInteractions(appointmentDao);
	}

	private AppointmentDto buildAppointmentDto() {
		AppointmentDto dto = new AppointmentDto();
		dto.setPetId(10L);
		dto.setVeterinarianId(20L);
		dto.setAppointmentDateTime(LocalDateTime.of(2026, 4, 28, 10, 0));
		dto.setReason("Checkup");
		dto.setStatus("Scheduled");
		return dto;
	}

	private Appointment buildAppointmentEntity(Long id) {
		Appointment appointment = new Appointment();
		appointment.setId(id);
		appointment.setPet(buildPetEntity(10L));
		appointment.setVeterinarian(buildVeterinarianEntity(20L));
		appointment.setAppointmentDateTime(LocalDateTime.of(2026, 4, 28, 10, 0));
		appointment.setReason("Checkup");
		appointment.setStatus("Scheduled");
		appointment.setCreatedAt(LocalDateTime.now());
		return appointment;
	}

	private Pet buildPetEntity(Long id) {
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
		pet.setId(id);
		pet.setCustomer(customer);
		pet.setBreed(breed);
		pet.setName("Milo");
		pet.setGender("Male");
		pet.setBirthDate(LocalDate.of(2021, 5, 10));
		pet.setColor("Brown");
		return pet;
	}

	private Veterinarian buildVeterinarianEntity(Long id) {
		Veterinarian veterinarian = new Veterinarian();
		veterinarian.setId(id);
		veterinarian.setFullName("Dr. Smith");
		veterinarian.setDocumentNumber("987654");
		return veterinarian;
	}
}
