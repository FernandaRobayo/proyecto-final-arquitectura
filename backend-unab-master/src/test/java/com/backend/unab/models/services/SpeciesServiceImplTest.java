package com.backend.unab.models.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.backend.unab.dto.SpeciesDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.ISpeciesDao;
import com.backend.unab.models.entity.Species;

@ExtendWith(MockitoExtension.class)
class SpeciesServiceImplTest {

	@Mock
	private ISpeciesDao speciesDao;

	@InjectMocks
	private SpeciesServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		SpeciesDto payload = buildSpeciesDto();
		when(speciesDao.save(any(Species.class))).thenAnswer(invocation -> {
			Species species = invocation.getArgument(0);
			species.setId(1L);
			return species;
		});

		SpeciesDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Canine", result.getName());
		verify(speciesDao).save(any(Species.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(new SpeciesDto()));

		assertEquals("Species name is required", exception.getMessage());
		verify(speciesDao, never()).save(any(Species.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Species entity = buildSpeciesEntity(1L, "Canine");
		SpeciesDto payload = new SpeciesDto();
		payload.setName("Feline");

		when(speciesDao.findById(1L)).thenReturn(Optional.of(entity));
		when(speciesDao.save(any(Species.class))).thenAnswer(invocation -> invocation.getArgument(0));

		SpeciesDto result = service.update(1L, payload);

		assertEquals("Feline", result.getName());
		verify(speciesDao).findById(1L);
		verify(speciesDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(speciesDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildSpeciesDto()));

		assertEquals("Species with id 99 not found", exception.getMessage());
		verify(speciesDao).findById(99L);
		verify(speciesDao, never()).save(any(Species.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Species entity = buildSpeciesEntity(1L, "Canine");
		when(speciesDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(speciesDao).findById(1L);
		verify(speciesDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(speciesDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Species with id 99 not found", exception.getMessage());
		verify(speciesDao).findById(99L);
		verify(speciesDao, never()).delete(any(Species.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(speciesDao.findById(1L)).thenReturn(Optional.of(buildSpeciesEntity(1L, "Canine")));

		SpeciesDto result = service.findById(1L);

		assertEquals("Canine", result.getName());
		verify(speciesDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(speciesDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Species with id 99 not found", exception.getMessage());
		verify(speciesDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(speciesDao.findAll()).thenReturn(List.of(buildSpeciesEntity(1L, "Canine"), buildSpeciesEntity(2L, "Feline")));

		List<SpeciesDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(speciesDao).findAll();
		verifyNoMoreInteractions(speciesDao);
	}

	private SpeciesDto buildSpeciesDto() {
		SpeciesDto dto = new SpeciesDto();
		dto.setName("Canine");
		return dto;
	}

	private Species buildSpeciesEntity(Long id, String name) {
		Species species = new Species();
		species.setId(id);
		species.setName(name);
		return species;
	}
}
