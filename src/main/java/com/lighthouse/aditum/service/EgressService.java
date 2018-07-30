package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Egress;
import com.lighthouse.aditum.repository.EgressRepository;
import com.lighthouse.aditum.service.dto.EgressDTO;
import com.lighthouse.aditum.service.mapper.EgressMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing Egress.
 */
@Service
@Transactional
public class EgressService {

    private final Logger log = LoggerFactory.getLogger(EgressService.class);

    private final EgressRepository egressRepository;

    private final EgressMapper egressMapper;

    private final EgressCategoryService egressCategoryService;



    public EgressService(EgressRepository egressRepository, EgressMapper egressMapper, EgressCategoryService egressCategoryService) {
        this.egressRepository = egressRepository;
        this.egressMapper = egressMapper;
        this.egressCategoryService = egressCategoryService;

    }

    /**
     * Save a egress.
     *
     * @param egressDTO the entity to save
     * @return the persisted entity
     */
    public EgressDTO save(EgressDTO egressDTO) {
        log.debug("Request to save Egress : {}", egressDTO);
        Egress egress = egressMapper.toEntity(egressDTO);
        egress = egressRepository.save(egress);
        return egressMapper.toDto(egress);
    }

    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompany(Pageable pageable,String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Egress> result = egressRepository.findByDatesBetweenAndCompany(pageable,zd_initialTime,zd_finalTime,companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }
    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompany(String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return new PageImpl<>(egressRepository.findByDatesBetweenAndCompany(zd_initialTime,zd_finalTime,companyId))
            .map(egressMapper::toDto);

    }
    @Transactional(readOnly = true)
    public Page<EgressDTO> findPaymentEgressByDatesBetweenAndCompany(String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return new PageImpl<>(egressRepository.findPaymentEgressByDatesBetweenAndCompany(zd_initialTime,zd_finalTime,companyId))
            .map(egressMapper::toDto);

    }
    @Transactional(readOnly = true)
    public Page<EgressDTO> findByCobroDatesBetweenAndCompany(Pageable pageable,String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Egress> result = egressRepository.findByCobroDatesBetweenAndCompany(pageable,zd_initialTime,zd_finalTime,companyId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }
    @Transactional(readOnly = true)
    public Page<EgressDTO> findByDatesBetweenAndCompanyAndAccount(Pageable pageable,String initialTime,String finalTime,Long companyId,String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<Egress> result = egressRepository.findByDatesBetweenAndCompanyAndAccount(pageable,zd_initialTime,zd_finalTime,companyId,accountId);
//        Collections.reverse(result);
        return result.map(egress -> egressMapper.toDto(egress));
    }
    @Transactional(readOnly = true)
    public List<EgressDTO> findByDatesBetweenAndCompanyAndAccount(String initialTime,String finalTime,Long companyId,String accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return egressRepository.findByDatesBetweenAndCompanyAndAccount(zd_initialTime,zd_finalTime,companyId,accountId).stream()
            .map(egressMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }
    /**
     *  Get all the egresses.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EgressDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all Egresses");
        Page<Egress> result = egressRepository.findByCompanyId(pageable,companyId);
        return result.map(egress -> egressMapper.toDto(egress));
    }

    /**
     *  Get one egress by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public EgressDTO findOne(Long id) {
        log.debug("Request to get Egress : {}", id);
        Egress egress = egressRepository.findOne(id);
        EgressDTO egreesDTO = egressMapper.toDto(egress);
        egreesDTO.setCategoryName(egressCategoryService.findOne(Long.parseLong(egreesDTO.getCategory())).getCategory());
        return egreesDTO;
    }

    /**
     *  Delete the  egress by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Egress : {}", id);
        egressRepository.delete(id);
    }
}
