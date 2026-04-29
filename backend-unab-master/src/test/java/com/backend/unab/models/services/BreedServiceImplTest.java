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

import com.backend.unab.dto.BreedDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IBreedDao;
import com.backend.unab.models.dao.ISpeciesDao;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Species;

@ExtendWith(MockitoExtension.class)
class BreedServiceImplTest {

	@Mock
	private IBreedDao breedDao;

	@Mock
	private ISpeciesDao speciesDao;

	@InjectMocks
	private BreedServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		BreedDto payload = buildBreedDto();
		when(speciesDao.findById(10L)).thenReturn(Optional.of(buildSpeciesEntity(10L, "Canine")));
		when(breedDao.save(any(Breed.class))).thenAnswer(invocation -> {
			Breed breed = invocation.getArgument(0);
			breed.setId(1L);
			return breed;
		});

		BreedDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Labrador", result.getName());
		assertEquals(10L, result.getSpeciesId());
		verify(speciesDao).findById(10L);
		verify(breedDao).save(any(Breed.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(new BreedDto()));

		assertEquals("Breed name is required", exception.getMessage());
		verify(breedDao, never()).save(any(Breed.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Breed entity = buildBreedEntity(1L, 10L, "Labrador", "Canine");
		BreedDto payload = buildBreedDto();
		payload.setName("Golden Retriever");

		when(breedDao.findById(1L)).thenReturn(Optional.of(entity));
		when(speciesDao.findById(10L)).thenReturn(Optional.of(buildSpeciesEntity(10L, "Canine")));
		when(breedDao.save(any(Breed.class))).thenAnswer(invocation -> invocation.getArgument(0));

		BreedDto result = service.update(1L, payload);

		assertEquals("Golden Retriever", result.getName());
		verify(breedDao).findById(1L);
		verify(speciesDao).findById(10L);
		verify(breedDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(breedDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildBreedDto()));

		assertEquals("Breed with id 99 not found", exception.getMessage());
		verify(breedDao).findById(99L);
		verify(breedDao, never()).save(any(Breed.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Breed entity = buildBreedEntity(1L, 10L, "Labrador", "Canine");
		when(breedDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(breedDao).findById(1L);
		verify(breedDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(breedDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Breed with id 99 not found", exception.getMessage());
		verify(breedDao).findById(99L);
		verify(breedDao, never()).delete(any(Breed.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(breedDao.findById(1L)).thenReturn(Optional.of(buildBreedEntity(1L, 10L, "Labrador", "Canine")));

		BreedDto result = service.findById(1L);

		assertEquals("Labrador", result.getName());
		assertEquals("Canine", result.getSpeciesName());
		verify(breedDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(breedDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Breed with id 99 not found", exception.getMessage());
		verify(breedDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(breedDao.findAll()).thenReturn(List.of(
				buildBreedEntity(1L, 10L, "Labrador", "Canine"),
				buildBreedEntity(2L, 11L, "Persian", "Feline")));

		List<BreedDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(breedDao).findAll();
		verifyNoMoreInteractions(breedDao);
	}

	private BreedDto buildBreedDto() {
		BreedDto dto = new BreedDto();
		dto.setName("Labrador");
		dto.setSpeciesId(10L);
		return dto;
	}

	private Species buildSpeciesEntity(Long id, String name) {
		Species species = new Species();
		species.setId(id);
		species.setName(name);
		return species;
	}

	private Breed buildBreedEntity(Long id, Long speciesId, String breedName, String speciesName) {
		Breed breed = new Breed();
		breed.setId(id);
		breed.setName(breedName);
		breed.setSpecies(buildSpeciesEntity(speciesId, speciesName));
		return breed;
	}
}
