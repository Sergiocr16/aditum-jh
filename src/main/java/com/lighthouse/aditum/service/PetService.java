package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Pet;
import com.lighthouse.aditum.repository.PetRepository;
import com.lighthouse.aditum.service.dto.PetDTO;
import com.lighthouse.aditum.service.mapper.PetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Pet.
 */
@Service
@Transactional
public class PetService {

    private final Logger log = LoggerFactory.getLogger(PetService.class);

    private final PetRepository petRepository;

    private final PetMapper petMapper;

    private final HouseService houseService;

    public PetService(HouseService houseService, PetRepository petRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.houseService = houseService;
    }

    /**
     * Save a pet.
     *
     * @param petDTO the entity to save
     * @return the persisted entity
     */
    public PetDTO save(PetDTO petDTO) {
        log.debug("Request to save Pet : {}", petDTO);
        Pet pet = petMapper.toEntity(petDTO);
        pet = petRepository.save(pet);
        return petMapper.toDto(pet);
    }

    /**
     * Get all the pets.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PetDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Pets");
        return petRepository.findAll(pageable)
            .map(petMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PetDTO> findAllByCompany(Pageable pageable, Long companyId, String name) {
        log.debug("Request to get all Pets");
        Page<PetDTO> pets = null;
        if (name.equals(" ")) {
            pets = petRepository.findAllByCompanyIdAndDeleted(pageable, companyId,0)
                .map(petMapper::toDto);
        } else {
            pets = petRepository.findAllByCompanyIdAndNameContainsAndDeleted(pageable, companyId, name,0)
                .map(petMapper::toDto);
        }
        pets.getContent().forEach(petDTO -> {
            petDTO.setHouseNumber(this.houseService.findOneClean(petDTO.getHouseId()).getHousenumber());
        });
        return pets;
    }

    @Transactional(readOnly = true)
    public Page<PetDTO> findAllByHouseId(Pageable pageable, Long houseId, String name) {
        log.debug("Request to get all Pets");
        Page<PetDTO> pets = null;
        if (name.equals(" ")) {
            pets = petRepository.findAllByHouseIdAndDeleted(pageable, houseId,0)
                .map(petMapper::toDto);
        } else {
            pets = petRepository.findAllByHouseIdAndNameContainsAndDeleted(pageable, houseId, name,0)
                .map(petMapper::toDto);
        }
        pets.getContent().forEach(petDTO -> {
            petDTO.setHouseNumber(this.houseService.findOneClean(petDTO.getHouseId()).getHousenumber());
        });
        return pets;
    }

    /**
     * Get one pet by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public PetDTO findOne(Long id) {
        log.debug("Request to get Pet : {}", id);
        Pet pet = petRepository.findOne(id);
        PetDTO petDTO = petMapper.toDto(pet);
        petDTO.setHouseNumber(this.houseService.findOneClean(petDTO.getHouseId()).getHousenumber());
        return petDTO;
    }

    /**
     * Delete the pet by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Pet : {}", id);
        PetDTO pet = this.findOne(id);
        pet.setDeleted(1);
        this.save(pet);
    }
}
