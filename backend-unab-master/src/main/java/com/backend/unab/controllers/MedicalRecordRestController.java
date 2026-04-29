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

import com.backend.unab.dto.MedicalRecordDto;
import com.backend.unab.models.services.IMedicalRecordService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordRestController {

	private final IMedicalRecordService medicalRecordService;

	public MedicalRecordRestController(IMedicalRecordService medicalRecordService) {
		this.medicalRecordService = medicalRecordService;
	}

	@PostMapping("")
	public ResponseEntity<MedicalRecordDto> create(@RequestBody MedicalRecordDto medicalRecordDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecordService.create(medicalRecordDto));
	}

	@PutMapping("{id}")
	public ResponseEntity<MedicalRecordDto> update(@PathVariable Long id, @RequestBody MedicalRecordDto medicalRecordDto) {
		return ResponseEntity.ok(medicalRecordService.update(id, medicalRecordDto));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		medicalRecordService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("{id}")
	public ResponseEntity<MedicalRecordDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(medicalRecordService.findById(id));
	}

	@GetMapping("")
	public ResponseEntity<List<MedicalRecordDto>> findAll() {
		return ResponseEntity.ok(medicalRecordService.findAll());
	}
}
