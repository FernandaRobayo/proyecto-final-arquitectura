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

import com.backend.unab.dto.TreatmentDto;
import com.backend.unab.models.services.ITreatmentService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/treatments")
public class TreatmentRestController {

	private final ITreatmentService treatmentService;

	public TreatmentRestController(ITreatmentService treatmentService) {
		this.treatmentService = treatmentService;
	}

	@PostMapping("")
	public ResponseEntity<TreatmentDto> create(@RequestBody TreatmentDto treatmentDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(treatmentService.create(treatmentDto));
	}

	@PutMapping("{id}")
	public ResponseEntity<TreatmentDto> update(@PathVariable Long id, @RequestBody TreatmentDto treatmentDto) {
		return ResponseEntity.ok(treatmentService.update(id, treatmentDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		treatmentService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<TreatmentDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(treatmentService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<TreatmentDto>> findAll() {
		return ResponseEntity.ok(treatmentService.findAll());
	}
}
