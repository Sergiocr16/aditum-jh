package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.MacroVisit;
import com.lighthouse.aditum.repository.MacroVisitRepository;
import com.lighthouse.aditum.service.dto.MacroVisitDTO;
import com.lighthouse.aditum.service.mapper.MacroVisitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing MacroVisit.
 */
@Service
@Transactional
public class MacroVisitService {

    private final Logger log = LoggerFactory.getLogger(MacroVisitService.class);

    private final MacroVisitRepository macroVisitRepository;

    private final MacroVisitMapper macroVisitMapper;

    public MacroVisitService(MacroVisitRepository macroVisitRepository, MacroVisitMapper macroVisitMapper) {
        this.macroVisitRepository = macroVisitRepository;
        this.macroVisitMapper = macroVisitMapper;
    }

    /**
     * Save a macroVisit.
     *
     * @param macroVisitDTO the entity to save
     * @return the persisted entity
     */
    public MacroVisitDTO save(MacroVisitDTO macroVisitDTO) {
        log.debug("Request to save MacroVisit : {}", macroVisitDTO);
        MacroVisit macroVisit = macroVisitMapper.toEntity(macroVisitDTO);
        macroVisit = macroVisitRepository.save(macroVisit);
        return macroVisitMapper.toDto(macroVisit);
    }

    /**
     * Get all the macroVisits.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MacroVisitDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MacroVisits");
        return macroVisitRepository.findAll(pageable)
            .map(macroVisitMapper::toDto);
    }
    /**
     * Get one macroVisit by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public MacroVisitDTO findOne(Long id) {
        log.debug("Request to get MacroVisit : {}", id);
        MacroVisit macroVisit = macroVisitRepository.findOne(id);
        return macroVisitMapper.toDto(macroVisit);
    }
    @Transactional(readOnly = true)
    public MacroVisitDTO findOneByMacroAndPlate(Long macroId,String plate) {
        log.debug("Request to get MacroVisit : {}", plate);
        MacroVisit macroVisit = macroVisitRepository.findFirstByMacroCondominiumIdAndLicenseplateOrderByIdDesc(macroId,plate);
        return macroVisitMapper.toDto(macroVisit);
    }
    @Transactional(readOnly = true)
    public MacroVisitDTO findOneByMacroAndIdentification(Long macroId,String identification) {
        log.debug("Request to get MacroVisit : {}", identification);
        MacroVisit macroVisit = macroVisitRepository.findFirstByMacroCondominiumIdAndIdentificationnumberOrderByIdDesc(macroId,identification);
        return macroVisitMapper.toDto(macroVisit);
    }
    /**
     * Delete the macroVisit by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MacroVisit : {}", id);
        macroVisitRepository.delete(id);
    }
}
