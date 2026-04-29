package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.CustomerDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.ICustomerDao;
import com.backend.unab.models.entity.Customer;

@Service
public class CustomerServiceImpl implements ICustomerService {

	private final ICustomerDao customerDao;

	public CustomerServiceImpl(ICustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	@Override
	@Transactional
	public CustomerDto create(CustomerDto customerDto) {
		validate(customerDto);
		Customer customer = new Customer();
		applyChanges(customer, customerDto);
		return toDto(customerDao.save(customer));
	}

	@Override
	@Transactional
	public CustomerDto update(Long id, CustomerDto customerDto) {
		validate(customerDto);
		Customer customer = findEntity(id);
		applyChanges(customer, customerDto);
		return toDto(customerDao.save(customer));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		customerDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerDto> findAll() {
		return customerDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Customer findEntity(Long id) {
		return customerDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
	}

	private void validate(CustomerDto customerDto) {
		if (customerDto == null) {
			throw new IllegalArgumentException("Customer payload is required");
		}
		if (!StringUtils.hasText(customerDto.getFullName())) {
			throw new IllegalArgumentException("Customer full name is required");
		}
		if (!StringUtils.hasText(customerDto.getDocumentNumber())) {
			throw new IllegalArgumentException("Customer document number is required");
		}
	}

	private void applyChanges(Customer customer, CustomerDto customerDto) {
		customer.setFullName(customerDto.getFullName().trim());
		customer.setDocumentNumber(customerDto.getDocumentNumber().trim());
		customer.setPhone(customerDto.getPhone());
		customer.setEmail(customerDto.getEmail());
		customer.setAddress(customerDto.getAddress());
	}

	private CustomerDto toDto(Customer customer) {
		CustomerDto dto = new CustomerDto();
		dto.setId(customer.getId());
		dto.setFullName(customer.getFullName());
		dto.setDocumentNumber(customer.getDocumentNumber());
		dto.setPhone(customer.getPhone());
		dto.setEmail(customer.getEmail());
		dto.setAddress(customer.getAddress());
		dto.setCreatedAt(customer.getCreatedAt());
		return dto;
	}
}
