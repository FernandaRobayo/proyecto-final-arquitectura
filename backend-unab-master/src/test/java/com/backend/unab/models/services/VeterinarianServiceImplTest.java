package com.backend.unab.models.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.unab.dto.VeterinarianDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IVeterinarianDao;
import com.backend.unab.models.entity.Veterinarian;

@ExtendWith(MockitoExtension.class)
class VeterinarianServiceImplTest {

	@Mock
	private IVeterinarianDao veterinarianDao;

	@InjectMocks
	private VeterinarianServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		VeterinarianDto payload = buildVeterinarianDto();
		when(veterinarianDao.save(any(Veterinarian.class))).thenAnswer(invocation -> {
			Veterinarian veterinarian = invocation.getArgument(0);
			veterinarian.setId(1L);
			veterinarian.setCreatedAt(LocalDateTime.now());
			return veterinarian;
		});

		VeterinarianDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Dr. Smith", result.getFullName());
		verify(veterinarianDao).save(any(Veterinarian.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		VeterinarianDto payload = new VeterinarianDto();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(payload));

		assertEquals("Veterinarian full name is required", exception.getMessage());
		verify(veterinarianDao, never()).save(any(Veterinarian.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Veterinarian entity = buildVeterinarianEntity(1L);
		VeterinarianDto payload = buildVeterinarianDto();
		payload.setSpecialty("Dermatology");

		when(veterinarianDao.findById(1L)).thenReturn(Optional.of(entity));
		when(veterinarianDao.save(any(Veterinarian.class))).thenAnswer(invocation -> invocation.getArgument(0));

		VeterinarianDto result = service.update(1L, payload);

		assertEquals("Dermatology", result.getSpecialty());
		verify(veterinarianDao).findById(1L);
		verify(veterinarianDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(veterinarianDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildVeterinarianDto()));

		assertEquals("Veterinarian with id 99 not found", exception.getMessage());
		verify(veterinarianDao).findById(99L);
		verify(veterinarianDao, never()).save(any(Veterinarian.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Veterinarian entity = buildVeterinarianEntity(1L);
		when(veterinarianDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(veterinarianDao).findById(1L);
		verify(veterinarianDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(veterinarianDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Veterinarian with id 99 not found", exception.getMessage());
		verify(veterinarianDao).findById(99L);
		verify(veterinarianDao, never()).delete(any(Veterinarian.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(veterinarianDao.findById(1L)).thenReturn(Optional.of(buildVeterinarianEntity(1L)));

		VeterinarianDto result = service.findById(1L);

		assertEquals("Dr. Smith", result.getFullName());
		verify(veterinarianDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(veterinarianDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Veterinarian with id 99 not found", exception.getMessage());
		verify(veterinarianDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(veterinarianDao.findAll()).thenReturn(List.of(buildVeterinarianEntity(1L), buildVeterinarianEntity(2L)));

		List<VeterinarianDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(veterinarianDao).findAll();
		verifyNoMoreInteractions(veterinarianDao);
	}

	private VeterinarianDto buildVeterinarianDto() {
		VeterinarianDto dto = new VeterinarianDto();
		dto.setFullName("Dr. Smith");
		dto.setDocumentNumber("987654");
		dto.setPhone("3001111111");
		dto.setEmail("smith@vet.local");
		dto.setSpecialty("Surgery");
		return dto;
	}

	private Veterinarian buildVeterinarianEntity(Long id) {
		Veterinarian veterinarian = new Veterinarian();
		veterinarian.setId(id);
		veterinarian.setFullName("Dr. Smith");
		veterinarian.setDocumentNumber("987654");
		veterinarian.setPhone("3001111111");
		veterinarian.setEmail("smith@vet.local");
		veterinarian.setSpecialty("Surgery");
		veterinarian.setCreatedAt(LocalDateTime.now());
		return veterinarian;
	}
}
