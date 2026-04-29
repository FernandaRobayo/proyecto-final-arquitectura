package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.BreedDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IBreedDao;
import com.backend.unab.models.dao.ISpeciesDao;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Species;

@Service
public class BreedServiceImpl implements IBreedService {

	private final IBreedDao breedDao;
	private final ISpeciesDao speciesDao;

	public BreedServiceImpl(IBreedDao breedDao, ISpeciesDao speciesDao) {
		this.breedDao = breedDao;
		this.speciesDao = speciesDao;
	}

	@Override
	@Transactional
	public BreedDto create(BreedDto breedDto) {
		validate(breedDto);
		Breed breed = new Breed();
		breed.setName(breedDto.getName().trim());
		breed.setSpecies(findSpecies(breedDto.getSpeciesId()));
		return toDto(breedDao.save(breed));
	}

	@Override
	@Transactional
	public BreedDto update(Long id, BreedDto breedDto) {
		validate(breedDto);
		Breed breed = findEntity(id);
		breed.setName(breedDto.getName().trim());
		breed.setSpecies(findSpecies(breedDto.getSpeciesId()));
		return toDto(breedDao.save(breed));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		breedDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public BreedDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<BreedDto> findAll() {
		return breedDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Breed findEntity(Long id) {
		return breedDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Breed with id " + id + " not found"));
	}

	private Species findSpecies(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Species id is required");
		}
		return speciesDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Species with id " + id + " not found"));
	}

	private void validate(BreedDto breedDto) {
		if (breedDto == null || !StringUtils.hasText(breedDto.getName())) {
			throw new IllegalArgumentException("Breed name is required");
		}
		if (breedDto.getSpeciesId() == null) {
			throw new IllegalArgumentException("Species id is required");
		}
	}

	private BreedDto toDto(Breed breed) {
		BreedDto dto = new BreedDto();
		dto.setId(breed.getId());
		dto.setName(breed.getName());
		dto.setSpeciesId(breed.getSpecies().getId());
		dto.setSpeciesName(breed.getSpecies().getName());
		return dto;
	}
}
