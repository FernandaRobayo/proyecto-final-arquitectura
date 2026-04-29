package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.VeterinarianDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IVeterinarianDao;
import com.backend.unab.models.entity.Veterinarian;

@Service
public class VeterinarianServiceImpl implements IVeterinarianService {

	private final IVeterinarianDao veterinarianDao;

	public VeterinarianServiceImpl(IVeterinarianDao veterinarianDao) {
		this.veterinarianDao = veterinarianDao;
	}

	@Override
	@Transactional
	public VeterinarianDto create(VeterinarianDto veterinarianDto) {
		validate(veterinarianDto);
		Veterinarian veterinarian = new Veterinarian();
		applyChanges(veterinarian, veterinarianDto);
		return toDto(veterinarianDao.save(veterinarian));
	}

	@Override
	@Transactional
	public VeterinarianDto update(Long id, VeterinarianDto veterinarianDto) {
		validate(veterinarianDto);
		Veterinarian veterinarian = findEntity(id);
		applyChanges(veterinarian, veterinarianDto);
		return toDto(veterinarianDao.save(veterinarian));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		veterinarianDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public VeterinarianDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<VeterinarianDto> findAll() {
		return veterinarianDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Veterinarian findEntity(Long id) {
		return veterinarianDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Veterinarian with id " + id + " not found"));
	}

	private void validate(VeterinarianDto veterinarianDto) {
		if (veterinarianDto == null) {
			throw new IllegalArgumentException("Veterinarian payload is required");
		}
		if (!StringUtils.hasText(veterinarianDto.getFullName())) {
			throw new IllegalArgumentException("Veterinarian full name is required");
		}
		if (!StringUtils.hasText(veterinarianDto.getDocumentNumber())) {
			throw new IllegalArgumentException("Veterinarian document number is required");
		}
	}

	private void applyChanges(Veterinarian veterinarian, VeterinarianDto veterinarianDto) {
		veterinarian.setFullName(veterinarianDto.getFullName().trim());
		veterinarian.setDocumentNumber(veterinarianDto.getDocumentNumber().trim());
		veterinarian.setPhone(veterinarianDto.getPhone());
		veterinarian.setEmail(veterinarianDto.getEmail());
		veterinarian.setSpecialty(veterinarianDto.getSpecialty());
	}

	private VeterinarianDto toDto(Veterinarian veterinarian) {
		VeterinarianDto dto = new VeterinarianDto();
		dto.setId(veterinarian.getId());
		dto.setFullName(veterinarian.getFullName());
		dto.setDocumentNumber(veterinarian.getDocumentNumber());
		dto.setPhone(veterinarian.getPhone());
		dto.setEmail(veterinarian.getEmail());
		dto.setSpecialty(veterinarian.getSpecialty());
		dto.setCreatedAt(veterinarian.getCreatedAt());
		return dto;
	}
}
