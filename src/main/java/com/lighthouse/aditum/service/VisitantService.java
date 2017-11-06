package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Visitant;
import com.lighthouse.aditum.repository.VisitantRepository;
import com.lighthouse.aditum.service.dto.VisitantDTO;
import com.lighthouse.aditum.service.mapper.VisitantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Visitant.
 */
@Service
@Transactional
public class VisitantService {

    private final Logger log = LoggerFactory.getLogger(VisitantService.class);

    private final VisitantRepository visitantRepository;

    private final VisitantMapper visitantMapper;

    public VisitantService(VisitantRepository visitantRepository, VisitantMapper visitantMapper) {
        this.visitantRepository = visitantRepository;
        this.visitantMapper = visitantMapper;
    }

    /**
     * Save a visitant.
     *
     * @param visitantDTO the entity to save
     * @return the persisted entity
     */
    public VisitantDTO save(VisitantDTO visitantDTO) {
        log.debug("Request to save Visitant : {}", visitantDTO);
        Visitant visitant = visitantMapper.visitantDTOToVisitant(visitantDTO);
        visitant = visitantRepository.save(visitant);
        VisitantDTO result = visitantMapper.visitantToVisitantDTO(visitant);
        return result;
    }

    /**
     *  Get all the visitants.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findAll(Long companyId) {
        log.debug("Request to get all Visitants");
        List<Visitant> result = visitantRepository.findByCompanyId(companyId);
        return new PageImpl<>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByDatesBetweenAndHouse(String initialTime,String finalTime,Long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        List<Visitant> result = visitantRepository.findByDatesBetweenAndHouse(zd_initialTime,zd_finalTime,houseId,3);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByHouseInLastMonth(Long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Visitant> result = visitantRepository.findByHouseInLastMonth(firstDayOfMonth,houseId,3);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }


    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByDatesBetweenAndCompany(String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        List<Visitant> result = visitantRepository.findByDatesBetweenAndCompany(zd_initialTime,zd_finalTime,companyId,3);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByCompanyInLastMonth(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Visitant> result = visitantRepository.findByCompanyInLastMonth(firstDayOfMonth,companyId,3);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }
    @Transactional(readOnly = true)
    public Integer countByCompanyInLastMonth(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return visitantRepository.countByCompanyInLastMonth(firstDayOfMonth,companyId,3);
    }

    @Transactional(readOnly = true)
    public Integer countByCompanyInLastDay(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return visitantRepository.countByCompanyInLastMonth(firstDayOfMonth,companyId,3);
    }

    @Transactional(readOnly = true)
    public ArrayList countVisitantsPerYear(Long companyId) {
        log.debug("Request to get all Visitants per month");
      ArrayList visitantesPorMes = new ArrayList();

      List<String> meses = Arrays.asList("Ene","Feb","Mar","Abr","May","Jun","Jul","Ago","Set","Oct","Nov","Dic");
       Integer currentMotnh = ZonedDateTime.now().minusHours(6).getMonthValue();
        for( int i = 1 ; i <= currentMotnh; i++){
            ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withMonth(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            ZonedDateTime lastDayOfMonth = ZonedDateTime.now().withMonth(i).withHour(23).withMinute(59).withSecond(59).withNano(59);
            int lastDay = lastDayOfMonth.getMonth().length(false);
            lastDayOfMonth = lastDayOfMonth.withDayOfMonth(lastDay);
            int visitantesEseMes = visitantRepository.countByDatesBetweenAndCompany(firstDayOfMonth,lastDayOfMonth,companyId,3);
            ArrayList info = new ArrayList();
            info.add(meses.get(i-1));
            info.add(visitantesEseMes);
            visitantesPorMes.add(info);
        }
        return visitantesPorMes;
    }

    @Transactional(readOnly = true)
    public ArrayList countVisitantsPerWeek(Long companyId) {
        log.debug("Request to get all Visitants per month");
        ArrayList visitantesPorSemana = new ArrayList();
        List<String> dias = Arrays.asList("Lun","Mar","Mier","Jue","Vie","Sab","Dom");
        Integer currentDay = ZonedDateTime.now().minusHours(6).getDayOfWeek().getValue();
        ZonedDateTime firstDayOfWeek = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(currentDay-1);
        for( int i = 0 ; i < currentDay; i++){
            ZonedDateTime initDay = firstDayOfWeek.plusDays(i);
            ZonedDateTime finishDay = firstDayOfWeek.withHour(23).withMinute(59).withSecond(59).withNano(59).plusDays(i);
            int visitantesEseDia = visitantRepository.countByDatesBetweenAndCompany(initDay,finishDay,companyId,3);
            ArrayList info = new ArrayList();
            info.add(dias.get(i));
            info.add(visitantesEseDia);
            visitantesPorSemana.add(info);
        }
        return visitantesPorSemana;
    }

    @Transactional(readOnly = true)
    public ArrayList countVisitantsPerMonth(Long companyId) {
        log.debug("Request to get all Visitants per month");
        ArrayList visitantesPorSemana = new ArrayList();
        Integer currentDay = ZonedDateTime.now().minusHours(6).getDayOfMonth();
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).withDayOfMonth(1);
        ZonedDateTime startDay = firstDayOfMonth;
        for( int i = 0 ; i < currentDay; i++){
            ZonedDateTime initDay = startDay.plusDays(i);
            ZonedDateTime finishDay = startDay.withHour(23).withMinute(59).withSecond(59).withNano(59).plusDays(i);
            int visitantesEseDia = visitantRepository.countByDatesBetweenAndCompany(initDay,finishDay,companyId,3);
            ArrayList info = new ArrayList();
            info.add(i+1);
            info.add(visitantesEseDia);
            visitantesPorSemana.add(info);
        }
        return visitantesPorSemana;
    }

    @Transactional(readOnly = true)
    public Integer countByAuthorizedVisitors(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        return visitantRepository.countByCompanyIdAndIsinvited(companyId,1);
    }
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findInvitedVisitorsByHouse(Long companyId, Long houseId) {
        log.debug("Request to get all Visitants");
        List<Visitant> result = visitantRepository.findByCompanyIdAndHouseIdAndIsinvitedOrCompanyIdAndHouseIdAndIsinvited(companyId,houseId,1,companyId,houseId,2);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findAllInvited(Long companyId) {
        log.debug("Request to get all Visitants");
        List<Visitant> result = visitantRepository.findByCompanyIdAndIsinvitedOrCompanyIdAndIsinvited(companyId,1,companyId,2);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }
    /**
     *  Get one visitant by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public VisitantDTO findOne(Long id) {
        log.debug("Request to get Visitant : {}", id);
        Visitant visitant = visitantRepository.findOne(id);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
        return visitantDTO;
    }

    @Transactional(readOnly = true)
    public VisitantDTO findInvitedVisitorByHouse(String identificationNumber,Long houseId,Long companyId) {
        log.debug("Request to find if there is already a registered visitor with this identification number : {}", identificationNumber);
        Visitant visitant = visitantRepository.findByIdentificationnumberAndHouseIdAndCompanyId(identificationNumber,houseId,companyId);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
        return visitantDTO;
    }

    /**
     *  Delete the  visitant by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Visitant : {}", id);
        visitantRepository.delete(id);
    }
}
