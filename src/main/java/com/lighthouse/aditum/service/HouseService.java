package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.HouseRepository;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing House.
 */
@Service
@Transactional
public class HouseService {

    private final Logger log = LoggerFactory.getLogger(HouseService.class);

    private final HouseRepository houseRepository;

    private final HouseMapper houseMapper;



    public HouseService(HouseRepository houseRepository, HouseMapper houseMapper) {
        this.houseRepository = houseRepository;
        this.houseMapper = houseMapper;
    }

    /**
     * Save a house.
     *
     * @param houseDTO the entity to save
     * @return the persisted entity
     */
    public HouseDTO save(HouseDTO houseDTO) {
        log.debug("Request to save House : {}", houseDTO);
        House house = houseMapper.houseDTOToHouse(houseDTO);
        house = houseRepository.save(house);
        HouseDTO result = houseMapper.houseToHouseDTO(house);
        return result;
    }


    /**
     *  Get all the houses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Houses");
        Page<House> result = houseRepository.findByCompanyId(pageable,companyId);
        return result.map(house -> houseMapper.houseToHouseDTO(house));
    }

    /**
     *  Get one house by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public HouseDTO findOne(Long id) {
        log.debug("Request to get House : {}", id);
        House house = houseRepository.findOne(id);
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);
        return houseDTO;
    }


    @Transactional(readOnly = true)
    public HouseDTO validateHouse(String houseNumber,String extension,Long companyId) {
        House rHouse = null;
        House house = houseRepository.findByhousenumberAndCompanyId(houseNumber,companyId);
        House house1 = houseRepository.findByExtensionAndCompanyId(extension,companyId);

        if(house!=null){
            rHouse= house;
        }
        if(house1!=null){
            rHouse= house1;
        }
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(rHouse);
        return houseDTO;
    }

    @Transactional(readOnly = true)
    public HouseDTO validateUpdatedHouse(Long houseId,String houseNumber,String extension,Long companyId) {
        House rHouse = null;
        House house = houseRepository.findByhousenumberAndAndCompanyIdAndIdNot(houseNumber,companyId,houseId);
        House house1 = houseRepository.findByExtensionAndCompanyIdAndIdNot(extension,companyId,houseId);

        if(house!=null){
            rHouse= house;
        }
        if(house1!=null){
            rHouse= house1;
        }
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(rHouse);
        return houseDTO;
    }





    /**
     *  Delete the  house by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete House : {}", id);
        houseRepository.delete(id);
    }
}
