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

import com.backend.unab.dto.BreedDto;
import com.backend.unab.models.services.IBreedService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/breeds")
public class BreedRestController {

	private final IBreedService breedService;

	public BreedRestController(IBreedService breedService) {
		this.breedService = breedService;
	}

	@PostMapping("")
	public ResponseEntity<BreedDto> create(@RequestBody BreedDto breedDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(breedService.create(breedDto));
	}

	@PutMapping("{id}")
	public ResponseEntity<BreedDto> update(@PathVariable Long id, @RequestBody BreedDto breedDto) {
		return ResponseEntity.ok(breedService.update(id, breedDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		breedService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<BreedDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(breedService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<BreedDto>> findAll() {
		return ResponseEntity.ok(breedService.findAll());
	}
}
