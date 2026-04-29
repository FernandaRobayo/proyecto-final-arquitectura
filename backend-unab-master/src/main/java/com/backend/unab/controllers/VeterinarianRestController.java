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

import com.backend.unab.dto.VeterinarianDto;
import com.backend.unab.models.services.IVeterinarianService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/veterinarians")
public class VeterinarianRestController {

	private final IVeterinarianService veterinarianService;

	public VeterinarianRestController(IVeterinarianService veterinarianService) {
		this.veterinarianService = veterinarianService;
	}

	@PostMapping("")
	public ResponseEntity<VeterinarianDto> create(@RequestBody VeterinarianDto veterinarianDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(veterinarianService.create(veterinarianDto));
	}

	@PutMapping("{id}")
	public ResponseEntity<VeterinarianDto> update(@PathVariable Long id, @RequestBody VeterinarianDto veterinarianDto) {
		return ResponseEntity.ok(veterinarianService.update(id, veterinarianDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		veterinarianService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<VeterinarianDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(veterinarianService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<VeterinarianDto>> findAll() {
		return ResponseEntity.ok(veterinarianService.findAll());
	}
}
