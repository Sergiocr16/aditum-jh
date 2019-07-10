package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Visitant;
import com.lighthouse.aditum.repository.VisitantRepository;
import com.lighthouse.aditum.service.dto.DashboardVisitorDTO;
import com.lighthouse.aditum.service.dto.VisitantDTO;
import com.lighthouse.aditum.service.mapper.VisitantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
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

    private final HouseService houseService;


    public VisitantService(VisitantRepository visitantRepository, VisitantMapper visitantMapper, @Lazy HouseService houseService) {
        this.visitantRepository = visitantRepository;
        this.visitantMapper = visitantMapper;
        this.houseService = houseService;
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
        if (visitant.getLicenseplate() == "NINGUNA") {
            visitant.setLicenseplate(null);
        }
        visitant = visitantRepository.save(visitant);
        VisitantDTO result = visitantMapper.visitantToVisitantDTO(visitant);
        return result;
    }

    /**
     * Get all the visitants.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findAll(Long companyId) {
        log.debug("Request to get all Visitants");
        List<Visitant> result = visitantRepository.findByCompanyId(companyId);
        return new PageImpl<>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByDatesBetweenAndHouse(ZonedDateTime initialTime, ZonedDateTime finalTime, Long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        List<Visitant> result = visitantRepository.findByArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvited(zd_initialTime, zd_finalTime, houseId, 3);
        Collections.reverse(result);
        return new PageImpl<>(result).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
            return visitantDTO;
        });
    }
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByDatesBetweenForAdmin(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        List<Visitant> result = visitantRepository.findByArrivaltimeAfterAndArrivaltimeBeforeAndCompanyIdAndIsinvited(zd_initialTime, zd_finalTime, companyId, 3);
        List<Visitant> result1 = new ArrayList<>();
        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).getResponsableofficer().equals("Oficina de administrador")){
                result1.add(result.get(i));
            }
        }
        Collections.reverse(result1);
        return new PageImpl<>(result1).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            return visitantDTO;
        });
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByFilter(Pageable pageable, Long companyId, String houseId,
                                          ZonedDateTime initialTime, ZonedDateTime finalTime, String name) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        Page<Visitant> result = new PageImpl<Visitant>(new ArrayList<Visitant>(),pageable,0);

        if (!name.equals("empty")) {
            if (houseId.equals("empty")) {
                result = visitantRepository.findByCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndNameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndLastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndSecondlastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndIdentificationnumberContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndLicenseplateContainsOrderByArrivaltimeDesc(
                    pageable, companyId, zd_initialTime, zd_finalTime, 3, name,
                    companyId, zd_initialTime, zd_finalTime, 3, name,
                    companyId, zd_initialTime, zd_finalTime, 3, name,
                    companyId, zd_initialTime, zd_finalTime, 3, name,
                    companyId, zd_initialTime, zd_finalTime, 3, name
                );
            } else {
                result = visitantRepository.findByArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndNameContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndLastnameContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndSecondlastnameContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndIdentificationnumberContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndLicenseplateContainsOrderByArrivaltimeDesc(
                    pageable, zd_initialTime, zd_finalTime, Long.parseLong(houseId), 3, name,
                    zd_initialTime, zd_finalTime, Long.parseLong(houseId),3, name,
                    zd_initialTime, zd_finalTime, Long.parseLong(houseId), 3, name,
                    zd_initialTime, zd_finalTime, Long.parseLong(houseId), 3, name,
                    zd_initialTime, zd_finalTime, Long.parseLong(houseId), 3, name
                );
            }
        }else{
            if (houseId.equals("empty")) {
                result = visitantRepository.findByCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedOrderByArrivaltimeDesc( pageable,companyId, zd_initialTime, zd_finalTime,  3);
            } else {
                result = visitantRepository.findByArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedOrderByArrivaltimeDesc(pageable,zd_initialTime, zd_finalTime,Long.parseLong(houseId),  3);
            }
        }
        return result.map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            if(visitant.getHouse()!=null){
                visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
            }else{
                visitantDTO.setHouseNumber(visitant.getResponsableofficer());
            }
            return visitantDTO;
        });
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByHouseInLastMonth(Long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Visitant> result = visitantRepository.findByHouseInLastMonth(firstDayOfMonth, houseId, 3);
        Collections.reverse(result);
        return new PageImpl<>(result).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            if(visitant.getHouse()!=null){
                visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
            }else{
                visitantDTO.setHouseNumber(visitant.getResponsableofficer());
            }
            return visitantDTO;
        });
    }
    @Transactional(readOnly = true)
    public Page<VisitantDTO> findForAdminInLastMonth(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Visitant> result = visitantRepository.findForAdminInLastMonth(firstDayOfMonth, companyId, 3);

        List<Visitant> result1 = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            if(result.get(i).getResponsableofficer().equals("Oficina de administrador")){
                result1.add(result.get(i));
            }
        }

        Collections.reverse(result1);
        return new PageImpl<>(result1).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);

            return visitantDTO;
        });
    }


    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByDatesBetweenAndCompany(ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withHour(0).withMinute(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withHour(23).withMinute(59).withSecond(59);
        List<Visitant> result = visitantRepository.findByDatesBetweenAndCompany(zd_initialTime, zd_finalTime, companyId, 3);
        Collections.reverse(result);
        return new PageImpl<>(result).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            if(visitant.getHouse()!=null){
                visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
            }else{
                visitantDTO.setHouseNumber(visitant.getResponsableofficer());
            }
            return visitantDTO;
        });
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findByCompanyInLastMonth(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Visitant> result = visitantRepository.findByCompanyInLastMonth(firstDayOfMonth, companyId, 3);
        Collections.reverse(result);
        return new PageImpl<>(result).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            if(visitant.getHouse()!=null){
                visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
            }else{
                visitantDTO.setHouseNumber(visitant.getResponsableofficer());
            }
            return visitantDTO;
        });
    }

    @Transactional(readOnly = true)
    public Integer countByCompanyInLastMonth(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        return visitantRepository.countByCompanyInLastMonth(firstDayOfMonth.plusHours(6), companyId, 3);
    }

    @Transactional(readOnly = true)
    public Integer countByCompanyInLastDay(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        return visitantRepository.countByCompanyInLastMonth(firstDayOfMonth.plusHours(6), companyId, 3);
    }

    @Transactional(readOnly = true)
    public ArrayList countVisitantsPerYear(Long companyId) {
        log.debug("Request to get all Visitants per month");
        ArrayList<DashboardVisitorDTO> visitantesPorMes = new ArrayList();

        List<String> meses = Arrays.asList("Ene", "Feb", "Mar", "Abr", "May", "Jun", "Jul", "Ago", "Set", "Oct", "Nov", "Dic");
        Integer currentMotnh = ZonedDateTime.now().getMonthValue();
        for (int i = 1; i <= currentMotnh; i++) {
            ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withMonth(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            ZonedDateTime lastDayOfMonth = ZonedDateTime.now().withMonth(i).withHour(23).withMinute(59).withSecond(59).withNano(59);
            int lastDay = lastDayOfMonth.getMonth().length(false);
            lastDayOfMonth = lastDayOfMonth.withDayOfMonth(lastDay);
            int visitantesEseMes = visitantRepository.countByDatesBetweenAndCompany(firstDayOfMonth.plusHours(6), lastDayOfMonth.plusHours(6), companyId, 3);
            DashboardVisitorDTO visitorInfo = new DashboardVisitorDTO();
            visitorInfo.setName(meses.get(i - 1));
            visitorInfo.setValue(visitantesEseMes);
            visitantesPorMes.add(visitorInfo);
        }
        return visitantesPorMes;
    }

    @Transactional(readOnly = true)
    public ArrayList countVisitantsPerWeek(Long companyId) {
        log.debug("Request to get all Visitants per month");
        ArrayList<DashboardVisitorDTO> visitantesPorSemana = new ArrayList();
        List<String> dias = Arrays.asList("Lun", "Mar", "Mier", "Jue", "Vie", "Sab", "Dom");
        Integer currentDay = ZonedDateTime.now().getDayOfWeek().getValue();
        ZonedDateTime firstDayOfWeek = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).minusDays(currentDay - 1);
        for (int i = 0; i < currentDay; i++) {
            ZonedDateTime initDay = firstDayOfWeek.plusDays(i);
            ZonedDateTime finishDay = firstDayOfWeek.withHour(23).withMinute(59).withSecond(59).withNano(59).plusDays(i);
            int visitantesEseDia = visitantRepository.countByDatesBetweenAndCompany(initDay, finishDay, companyId, 3);
            DashboardVisitorDTO visitorInfo = new DashboardVisitorDTO();
            visitorInfo.setName(dias.get(i));
            visitorInfo.setValue(visitantesEseDia);
            visitantesPorSemana.add(visitorInfo);
        }
        return visitantesPorSemana;
    }

    @Transactional(readOnly = true)
    public ArrayList countVisitantsPerMonth(Long companyId) {
        log.debug("Request to get all Visitants per month");
        ArrayList<DashboardVisitorDTO> visitantesPorSemana = new ArrayList();
        Integer currentDay = ZonedDateTime.now().getDayOfMonth();
        ZonedDateTime firstDayOfMonth = ZonedDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).withDayOfMonth(1);
        ZonedDateTime startDay = firstDayOfMonth;
        for (int i = 0; i < currentDay; i++) {
            ZonedDateTime initDay = startDay.plusDays(i);
            ZonedDateTime finishDay = startDay.withHour(23).withMinute(59).withSecond(59).withNano(59).plusDays(i);
            int visitantesEseDia = visitantRepository.countByDatesBetweenAndCompany(initDay.plusHours(6), finishDay.plusHours(6), companyId, 3);
            DashboardVisitorDTO visitorInfo = new DashboardVisitorDTO();
            visitorInfo.setName(i + 1 + "");
            visitorInfo.setValue(visitantesEseDia);
            visitantesPorSemana.add(visitorInfo);
        }
        return visitantesPorSemana;
    }

    @Transactional(readOnly = true)
    public Integer countByAuthorizedVisitors(Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        return visitantRepository.countByCompanyIdAndIsinvited(companyId, 1);
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findInvitedVisitorsByHouse(Long companyId, Long houseId) {
        log.debug("Request to get all Visitants");
        List<Visitant> result = visitantRepository.findByCompanyIdAndHouseIdAndIsinvitedOrCompanyIdAndHouseIdAndIsinvited(companyId, houseId, 1, companyId, houseId, 2);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> visitantMapper.visitantToVisitantDTO(visitant));
    }

    @Transactional(readOnly = true)
    public Page<VisitantDTO> findAllInvited(Long companyId) {
        log.debug("Request to get all Visitants");
        List<Visitant> result = visitantRepository.findByCompanyIdAndIsinvitedOrCompanyIdAndIsinvited(companyId, 1, companyId, 2);
        Collections.reverse(result);
        return new PageImpl<Visitant>(result).map(visitant -> {
            VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
            if(visitant.getHouse()!=null){
                visitantDTO.setHouseNumber(this.houseService.findOne(visitant.getHouse().getId()).getHousenumber());
            }else{
                visitantDTO.setHouseNumber(visitant.getResponsableofficer());
            }
            return visitantDTO;
        });
    }

    /**
     * Get one visitant by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public VisitantDTO findOne(Long id) {
        log.debug("Request to get Visitant : {}", id);
        Visitant visitant = visitantRepository.findOne(id);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
        return visitantDTO;
    }

    @Transactional(readOnly = true)
    public VisitantDTO findInvitedVisitorByHouse(String identificationNumber, Long houseId, Long companyId) {
        log.debug("Request to find if there is already a registered visitor with this identification number : {}", identificationNumber);
        Visitant visitant = visitantRepository.findByIdentificationnumberAndHouseIdAndCompanyId(identificationNumber, houseId, companyId);
        VisitantDTO visitantDTO = visitantMapper.visitantToVisitantDTO(visitant);
        return visitantDTO;
    }

    /**
     * Delete the  visitant by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Visitant : {}", id);
        visitantRepository.delete(id);
    }
}
