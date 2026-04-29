package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.SpeciesDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.ISpeciesDao;
import com.backend.unab.models.entity.Species;

@Service
public class SpeciesServiceImpl implements ISpeciesService {

	private final ISpeciesDao speciesDao;

	public SpeciesServiceImpl(ISpeciesDao speciesDao) {
		this.speciesDao = speciesDao;
	}

	@Override
	@Transactional
	public SpeciesDto create(SpeciesDto speciesDto) {
		validate(speciesDto);
		Species species = new Species();
		species.setName(speciesDto.getName().trim());
		return toDto(speciesDao.save(species));
	}

	@Override
	@Transactional
	public SpeciesDto update(Long id, SpeciesDto speciesDto) {
		validate(speciesDto);
		Species species = findEntity(id);
		species.setName(speciesDto.getName().trim());
		return toDto(speciesDao.save(species));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		speciesDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public SpeciesDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<SpeciesDto> findAll() {
		return speciesDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Species findEntity(Long id) {
		return speciesDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Species with id " + id + " not found"));
	}

	private void validate(SpeciesDto speciesDto) {
		if (speciesDto == null || !StringUtils.hasText(speciesDto.getName())) {
			throw new IllegalArgumentException("Species name is required");
		}
	}

	private SpeciesDto toDto(Species species) {
		SpeciesDto dto = new SpeciesDto();
		dto.setId(species.getId());
		dto.setName(species.getName());
		return dto;
	}
}
