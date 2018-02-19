package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.repository.HouseRepository;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.mapper.HouseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<HouseDTO> findAll(Long companyId) {
        log.debug("Request to get all Houses");
        List<House> result = houseRepository.findByCompanyId(companyId);
        List<House> onlyHouses = new ArrayList<>();
        Character [] letras = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u','v','w','x','y','z'};

        result.forEach(house->{
            int existe = 0;
              for (int i = 0;i<letras.length;i++){
                 if(Character.toLowerCase(house.getHousenumber().charAt(0))==(letras[i])){
                    existe++;
                 }
              }
              if(existe==0){
                  onlyHouses.add(house);
              }
        });

        return  new PageImpl<>(onlyHouses).map(house -> houseMapper.houseToHouseDTO(house));
    }

    @Transactional(readOnly = true)
    public Page<HouseDTO> findAllWithMaintenance(Long companyId) {
        log.debug("Request to get all Houses");
        List<House> result = houseRepository.findByCompanyId(companyId);
        List<House> onlyHouses = new ArrayList<>();
        Character [] letras = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','ñ','o','p','q','r','s','t','u','v','w','x','y','z'};

        result.forEach(house->{
            int existe = 0;
            for (int i = 0;i<letras.length;i++){
                if(Character.toLowerCase(house.getHousenumber().charAt(0))==(letras[i])){
                    onlyHouses.add(house);
                }
            }
        });
        return  new PageImpl<>(result).map(house -> houseMapper.houseToHouseDTO(house));
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
        houseDTO.setCodeStatus(house.getCodeStatus());

        return houseDTO;
    }


    @Transactional(readOnly = true)
    public HouseDTO findByLoginCodde(String loginCode) {
        log.debug("Request to get House : {}", loginCode);
        House house = houseRepository.findByLoginCode(loginCode);
        HouseDTO houseDTO = houseMapper.houseToHouseDTO(house);
        houseDTO.setCodeStatus(house.getCodeStatus());
        return houseDTO;
    }

    @Transactional(readOnly = true)
    public Integer countByCompany(Long companyid) {
        log.debug("Request to get House : {}", companyid);
        return houseRepository.countByCompanyId(companyid);
    }

    @Transactional(readOnly = true)
    public Integer countByCompanyAndDesocupated(Long companyid) {
        log.debug("Request to get House : {}", companyid);
        return houseRepository.countByCompanyIdAndIsdesocupated(companyid,1);
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
        houseDTO.setCodeStatus(house.getCodeStatus());
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
        houseDTO.setCodeStatus(house.getCodeStatus());
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
