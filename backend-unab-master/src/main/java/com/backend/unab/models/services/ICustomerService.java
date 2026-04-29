package com.backend.unab.models.services;

import java.util.List;

import com.backend.unab.dto.CustomerDto;

public interface ICustomerService {

	CustomerDto create(CustomerDto customerDto);

	CustomerDto update(Long id, CustomerDto customerDto);

	void delete(Long id);

	CustomerDto findById(Long id);

	List<CustomerDto> findAll();
}
