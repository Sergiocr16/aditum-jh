package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.MacroVisit;
import com.lighthouse.aditum.repository.MacroVisitRepository;
import com.lighthouse.aditum.service.dto.MacroVisitDTO;
import com.lighthouse.aditum.service.mapper.MacroVisitMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;


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
    public Page<MacroVisitDTO> findByFilter(Pageable pageable, Long macroCondominiumId, String companyId,
                                          ZonedDateTime initialTime, ZonedDateTime finalTime, String name) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        Page<MacroVisit> result = new PageImpl<MacroVisit>(new ArrayList<MacroVisit>(),pageable,0);

        if (!name.equals("empty")) {
            if (companyId.equals("empty")) {
                result = macroVisitRepository.findByMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndNameContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLastnameContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndSecondlastnameContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIdentificationnumberContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLicenseplateContainsOrderByArrivaltimeDesc(
                    pageable, macroCondominiumId, zd_initialTime, zd_finalTime, name,
                    macroCondominiumId, zd_initialTime, zd_finalTime, name,
                    macroCondominiumId, zd_initialTime, zd_finalTime, name,
                    macroCondominiumId, zd_initialTime, zd_finalTime, name,
                    macroCondominiumId, zd_initialTime, zd_finalTime, name
                );
            } else {
                result = macroVisitRepository.findByCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndNameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndSecondlastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIdentificationnumberContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLicenseplateContainsOrderByArrivaltimeDesc(
                    pageable, Long.parseLong(companyId), zd_initialTime, zd_finalTime, name,
                    Long.parseLong(companyId), zd_initialTime, zd_finalTime, name,
                    Long.parseLong(companyId), zd_initialTime, zd_finalTime, name,
                    Long.parseLong(companyId), zd_initialTime, zd_finalTime, name,
                    Long.parseLong(companyId), zd_initialTime, zd_finalTime, name
                );
            }
        }else{
            if (companyId.equals("empty")) {
                result = macroVisitRepository.findByMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeOrderByArrivaltimeDesc( pageable,macroCondominiumId, zd_initialTime, zd_finalTime);
            } else {
                result = macroVisitRepository.findByArrivaltimeAfterAndArrivaltimeBeforeAndCompanyIdOrderByArrivaltimeDesc(pageable,zd_initialTime, zd_finalTime,Long.parseLong(companyId));
            }
        }
        return result.map(visitant -> {
            MacroVisitDTO visitantDTO = macroVisitMapper.toDto(visitant);
//            if(visitant.getCompany()!=null){
//                visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
//            }else{
//                visitantDTO.setHouseNumber(visitant.getResponsableofficer());
//            }
            return visitantDTO;
        });
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
