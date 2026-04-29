package com.backend.unab.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.backend.unab.dto.PetDto;
import com.backend.unab.exception.ResourceNotFoundException;
import com.backend.unab.models.dao.IBreedDao;
import com.backend.unab.models.dao.ICustomerDao;
import com.backend.unab.models.dao.IPetDao;
import com.backend.unab.models.entity.Breed;
import com.backend.unab.models.entity.Customer;
import com.backend.unab.models.entity.Pet;

@Service
public class PetServiceImpl implements IPetService {

	private final IPetDao petDao;
	private final ICustomerDao customerDao;
	private final IBreedDao breedDao;

	public PetServiceImpl(IPetDao petDao, ICustomerDao customerDao, IBreedDao breedDao) {
		this.petDao = petDao;
		this.customerDao = customerDao;
		this.breedDao = breedDao;
	}

	@Override
	@Transactional
	public PetDto create(PetDto petDto) {
		validate(petDto);
		Pet pet = new Pet();
		applyChanges(pet, petDto);
		return toDto(petDao.save(pet));
	}

	@Override
	@Transactional
	public PetDto update(Long id, PetDto petDto) {
		validate(petDto);
		Pet pet = findEntity(id);
		applyChanges(pet, petDto);
		return toDto(petDao.save(pet));
	}

	@Override
	@Transactional
	public void delete(Long id) {
		petDao.delete(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public PetDto findById(Long id) {
		return toDto(findEntity(id));
	}

	@Override
	@Transactional(readOnly = true)
	public List<PetDto> findAll() {
		return petDao.findAll().stream().map(this::toDto).collect(Collectors.toList());
	}

	private Pet findEntity(Long id) {
		return petDao.findById(id).orElseThrow(() -> new ResourceNotFoundException("Pet with id " + id + " not found"));
	}

	private Customer findCustomer(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Customer id is required");
		}
		return customerDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Customer with id " + id + " not found"));
	}

	private Breed findBreed(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("Breed id is required");
		}
		return breedDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Breed with id " + id + " not found"));
	}

	private void validate(PetDto petDto) {
		if (petDto == null) {
			throw new IllegalArgumentException("Pet payload is required");
		}
		if (!StringUtils.hasText(petDto.getName())) {
			throw new IllegalArgumentException("Pet name is required");
		}
		if (petDto.getCustomerId() == null) {
			throw new IllegalArgumentException("Customer id is required");
		}
		if (petDto.getBreedId() == null) {
			throw new IllegalArgumentException("Breed id is required");
		}
	}

	private void applyChanges(Pet pet, PetDto petDto) {
		pet.setCustomer(findCustomer(petDto.getCustomerId()));
		pet.setBreed(findBreed(petDto.getBreedId()));
		pet.setName(petDto.getName().trim());
		pet.setGender(petDto.getGender());
		pet.setBirthDate(petDto.getBirthDate());
		pet.setColor(petDto.getColor());
	}

	private PetDto toDto(Pet pet) {
		PetDto dto = new PetDto();
		dto.setId(pet.getId());
		dto.setCustomerId(pet.getCustomer().getId());
		dto.setCustomerFullName(pet.getCustomer().getFullName());
		dto.setBreedId(pet.getBreed().getId());
		dto.setBreedName(pet.getBreed().getName());
		dto.setName(pet.getName());
		dto.setGender(pet.getGender());
		dto.setBirthDate(pet.getBirthDate());
		dto.setColor(pet.getColor());
		dto.setCreatedAt(pet.getCreatedAt());
		return dto;
	}
}
