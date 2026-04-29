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

import com.backend.unab.dto.SpeciesDto;
import com.backend.unab.models.services.ISpeciesService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/species")
public class SpeciesRestController {

	private final ISpeciesService speciesService;

	public SpeciesRestController(ISpeciesService speciesService) {
		this.speciesService = speciesService;
	}

	@PostMapping("")
	public ResponseEntity<SpeciesDto> create(@RequestBody SpeciesDto speciesDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(speciesService.create(speciesDto));
	}

	@PutMapping("{id}")
	public ResponseEntity<SpeciesDto> update(@PathVariable Long id, @RequestBody SpeciesDto speciesDto) {
		return ResponseEntity.ok(speciesService.update(id, speciesDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		speciesService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<SpeciesDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(speciesService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<SpeciesDto>> findAll() {
		return ResponseEntity.ok(speciesService.findAll());
	}
}
