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

import com.backend.unab.dto.PetDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IBreedDao;
import com.backend.unab.models.dao.ICustomerDao;
import com.backend.unab.models.dao.IPetDao;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Customer;
import com.backend.unab.models.entity.Pet;
import com.backend.unab.models.entity.Species;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

	@Mock
	private IPetDao petDao;

	@Mock
	private ICustomerDao customerDao;

	@Mock
	private IBreedDao breedDao;

	@InjectMocks
	private PetServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		PetDto payload = buildPetDto();
		when(customerDao.findById(10L)).thenReturn(Optional.of(buildCustomerEntity(10L)));
		when(breedDao.findById(20L)).thenReturn(Optional.of(buildBreedEntity(20L)));
		when(petDao.save(any(Pet.class))).thenAnswer(invocation -> {
			Pet pet = invocation.getArgument(0);
			pet.setId(1L);
			pet.setCreatedAt(LocalDateTime.now());
			return pet;
		});

		PetDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("Milo", result.getName());
		assertEquals("John Doe", result.getCustomerFullName());
		verify(customerDao).findById(10L);
		verify(breedDao).findById(20L);
		verify(petDao).save(any(Pet.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		PetDto payload = new PetDto();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(payload));

		assertEquals("Pet name is required", exception.getMessage());
		verify(petDao, never()).save(any(Pet.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Pet entity = buildPetEntity(1L);
		PetDto payload = buildPetDto();
		payload.setName("Luna");

		when(petDao.findById(1L)).thenReturn(Optional.of(entity));
		when(customerDao.findById(10L)).thenReturn(Optional.of(buildCustomerEntity(10L)));
		when(breedDao.findById(20L)).thenReturn(Optional.of(buildBreedEntity(20L)));
		when(petDao.save(any(Pet.class))).thenAnswer(invocation -> invocation.getArgument(0));

		PetDto result = service.update(1L, payload);

		assertEquals("Luna", result.getName());
		verify(petDao).findById(1L);
		verify(customerDao).findById(10L);
		verify(breedDao).findById(20L);
		verify(petDao).save(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		when(petDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, buildPetDto()));

		assertEquals("Pet with id 99 not found", exception.getMessage());
		verify(petDao).findById(99L);
		verify(petDao, never()).save(any(Pet.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Pet entity = buildPetEntity(1L);
		when(petDao.findById(1L)).thenReturn(Optional.of(entity));

		service.delete(1L);

		verify(petDao).findById(1L);
		verify(petDao).delete(entity);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(petDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Pet with id 99 not found", exception.getMessage());
		verify(petDao).findById(99L);
		verify(petDao, never()).delete(any(Pet.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		when(petDao.findById(1L)).thenReturn(Optional.of(buildPetEntity(1L)));

		PetDto result = service.findById(1L);

		assertEquals("Milo", result.getName());
		assertEquals("Labrador", result.getBreedName());
		verify(petDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(petDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Pet with id 99 not found", exception.getMessage());
		verify(petDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(petDao.findAll()).thenReturn(List.of(buildPetEntity(1L), buildPetEntity(2L)));

		List<PetDto> result = service.findAll();

		assertEquals(2, result.size());
		verify(petDao).findAll();
		verifyNoMoreInteractions(petDao);
	}

	private PetDto buildPetDto() {
		PetDto dto = new PetDto();
		dto.setCustomerId(10L);
		dto.setBreedId(20L);
		dto.setName("Milo");
		dto.setGender("Male");
		dto.setBirthDate(LocalDate.of(2021, 5, 10));
		dto.setColor("Brown");
		return dto;
	}

	private Customer buildCustomerEntity(Long id) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setFullName("John Doe");
		customer.setDocumentNumber("123456");
		return customer;
	}

	private Breed buildBreedEntity(Long id) {
		Species species = new Species();
		species.setId(30L);
		species.setName("Canine");

		Breed breed = new Breed();
		breed.setId(id);
		breed.setName("Labrador");
		breed.setSpecies(species);
		return breed;
	}

	private Pet buildPetEntity(Long id) {
		Pet pet = new Pet();
		pet.setId(id);
		pet.setCustomer(buildCustomerEntity(10L));
		pet.setBreed(buildBreedEntity(20L));
		pet.setName("Milo");
		pet.setGender("Male");
		pet.setBirthDate(LocalDate.of(2021, 5, 10));
		pet.setColor("Brown");
		pet.setCreatedAt(LocalDateTime.now());
		return pet;
	}
}
