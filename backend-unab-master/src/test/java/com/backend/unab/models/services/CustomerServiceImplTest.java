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

import com.backend.unab.dto.CustomerDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.ICustomerDao;
import com.backend.unab.models.entity.Customer;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private ICustomerDao customerDao;

	@InjectMocks
	private CustomerServiceImpl service;

	@Test
	void shouldCreateEntitySuccessfully() {
		CustomerDto payload = buildCustomerDto();
		when(customerDao.save(any(Customer.class))).thenAnswer(invocation -> {
			Customer customer = invocation.getArgument(0);
			customer.setId(1L);
			customer.setCreatedAt(LocalDateTime.now());
			return customer;
		});

		CustomerDto result = service.create(payload);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("John Doe", result.getFullName());
		assertEquals("123456", result.getDocumentNumber());
		verify(customerDao).save(any(Customer.class));
	}

	@Test
	void shouldThrowExceptionWhenCreatePayloadIsInvalid() {
		CustomerDto payload = new CustomerDto();

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> service.create(payload));

		assertEquals("Customer full name is required", exception.getMessage());
		verify(customerDao, never()).save(any(Customer.class));
	}

	@Test
	void shouldUpdateEntitySuccessfully() {
		Customer customer = buildCustomerEntity(1L);
		CustomerDto payload = buildCustomerDto();
		payload.setFullName("Jane Doe");

		when(customerDao.findById(1L)).thenReturn(Optional.of(customer));
		when(customerDao.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

		CustomerDto result = service.update(1L, payload);

		assertNotNull(result);
		assertEquals("Jane Doe", result.getFullName());
		verify(customerDao).findById(1L);
		verify(customerDao).save(customer);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnUpdate() {
		CustomerDto payload = buildCustomerDto();
		when(customerDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception =
				assertThrows(ResourceNotFoundException.class, () -> service.update(99L, payload));

		assertEquals("Customer with id 99 not found", exception.getMessage());
		verify(customerDao).findById(99L);
		verify(customerDao, never()).save(any(Customer.class));
	}

	@Test
	void shouldDeleteEntitySuccessfully() {
		Customer customer = buildCustomerEntity(1L);
		when(customerDao.findById(1L)).thenReturn(Optional.of(customer));

		service.delete(1L);

		verify(customerDao).findById(1L);
		verify(customerDao).delete(customer);
	}

	@Test
	void shouldThrowExceptionWhenEntityNotFoundOnDelete() {
		when(customerDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(99L));

		assertEquals("Customer with id 99 not found", exception.getMessage());
		verify(customerDao).findById(99L);
		verify(customerDao, never()).delete(any(Customer.class));
	}

	@Test
	void shouldReturnEntityWhenExists() {
		Customer customer = buildCustomerEntity(1L);
		when(customerDao.findById(1L)).thenReturn(Optional.of(customer));

		CustomerDto result = service.findById(1L);

		assertNotNull(result);
		assertEquals(1L, result.getId());
		assertEquals("John Doe", result.getFullName());
		verify(customerDao).findById(1L);
	}

	@Test
	void shouldThrowExceptionWhenNotFound() {
		when(customerDao.findById(99L)).thenReturn(Optional.empty());

		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.findById(99L));

		assertEquals("Customer with id 99 not found", exception.getMessage());
		verify(customerDao).findById(99L);
	}

	@Test
	void shouldReturnList() {
		when(customerDao.findAll()).thenReturn(List.of(buildCustomerEntity(1L), buildCustomerEntity(2L)));

		List<CustomerDto> result = service.findAll();

		assertNotNull(result);
		assertEquals(2, result.size());
		verify(customerDao).findAll();
		verifyNoMoreInteractions(customerDao);
	}

	private CustomerDto buildCustomerDto() {
		CustomerDto dto = new CustomerDto();
		dto.setFullName("John Doe");
		dto.setDocumentNumber("123456");
		dto.setPhone("3000000000");
		dto.setEmail("john@vet.local");
		dto.setAddress("Street 123");
		return dto;
	}

	private Customer buildCustomerEntity(Long id) {
		Customer customer = new Customer();
		customer.setId(id);
		customer.setFullName("John Doe");
		customer.setDocumentNumber("123456");
		customer.setPhone("3000000000");
		customer.setEmail("john@vet.local");
		customer.setAddress("Street 123");
		customer.setCreatedAt(LocalDateTime.now());
		return customer;
	}
}
