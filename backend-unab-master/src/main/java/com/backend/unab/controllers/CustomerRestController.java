package com.backend.unab.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.unab.dto.CustomerDto;
import com.backend.unab.models.services.ICustomerService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/customers")
public class CustomerRestController {

	private final ICustomerService customerService;

	public CustomerRestController(ICustomerService customerService) {
		this.customerService = customerService;
	}

	@PostMapping("")
	public ResponseEntity<CustomerDto> create(@RequestBody CustomerDto customerDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(customerService.create(customerDto));
	}

	@PutMapping("{id}")
	public ResponseEntity<CustomerDto> update(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
		return ResponseEntity.ok(customerService.update(id, customerDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		customerService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<CustomerDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(customerService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<CustomerDto>> findAll() {
		return ResponseEntity.ok(customerService.findAll());
	}
}
