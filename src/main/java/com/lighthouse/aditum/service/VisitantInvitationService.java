package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.InvitationSchedule;
import com.lighthouse.aditum.domain.VisitantInvitation;
import com.lighthouse.aditum.repository.VisitantInvitationRepository;
import com.lighthouse.aditum.service.dto.InvitationScheduleDTO;
import com.lighthouse.aditum.service.dto.VisitantInvitationDTO;
import com.lighthouse.aditum.service.mapper.VisitantInvitationMapper;
import com.lighthouse.aditum.web.websocket.RealTimeCompanyInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;


/**
 * Service Implementation for managing VisitantInvitation.
 */
@Service
@Transactional
public class VisitantInvitationService {

    private final Logger log = LoggerFactory.getLogger(VisitantInvitationService.class);

    private final VisitantInvitationRepository visitantInvitationRepository;

    private final VisitantInvitationMapper visitantInvitationMapper;

    private final BitacoraAccionesService bitacoraAccionesService;


    private final InvitationScheduleService invitationScheduleService;

    private final MacroCondominiumService macroCondominiumService;

    private final HouseService houseService;

    public VisitantInvitationService(@Lazy HouseService houseService, MacroCondominiumService macroCondominiumService, BitacoraAccionesService bitacoraAccionesService, InvitationScheduleService invitationScheduleService, VisitantInvitationRepository visitantInvitationRepository, VisitantInvitationMapper visitantInvitationMapper) {
        this.visitantInvitationRepository = visitantInvitationRepository;
        this.visitantInvitationMapper = visitantInvitationMapper;
        this.invitationScheduleService = invitationScheduleService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.macroCondominiumService = macroCondominiumService;
        this.houseService = houseService;
    }

    /**
     * Save a visitantInvitation.
     *
     * @param visitantInvitationDTO the entity to save
     * @return the persisted entity
     */
    public VisitantInvitationDTO save(VisitantInvitationDTO visitantInvitationDTO) {
        log.debug("Request to save VisitantInvitation : {}", visitantInvitationDTO);
        VisitantInvitation visitantInvitation = visitantInvitationMapper.toEntity(visitantInvitationDTO);
        visitantInvitation = visitantInvitationRepository.save(visitantInvitation);

        String concepto = "Invitación al visitante: " + visitantInvitation.getName() + " " + visitantInvitation.getSecondlastname();
        bitacoraAccionesService.save(createBitacoraAcciones(concepto,10, null,"Visitantes",visitantInvitation.getId(),visitantInvitation.getCompanyId(),visitantInvitation.getHouseId()));
        VisitantInvitationDTO visitantInvitationDTO1 = visitantInvitationMapper.toDto(visitantInvitation);
        return visitantInvitationDTO1;
    }

    @Transactional(readOnly = true)
    public Page<VisitantInvitationDTO> findInvitedVisitorsByHouse(Long companyId, Long houseId, int timeFormat) {
        log.debug("Request to get all Visitants");
        List<VisitantInvitation> result = visitantInvitationRepository.findByCompanyIdAndHouseIdAndStatusAndHasscheduleOrCompanyIdAndHouseIdAndStatusAndHasschedule(companyId, houseId, 1, timeFormat, companyId, houseId, 2, timeFormat);
        Collections.reverse(result);
        return new PageImpl<VisitantInvitation>(result).map(visitant -> visitantInvitationMapper.toDto(visitant));
    }

    @Transactional(readOnly = true)
    public Page<VisitantInvitationDTO> findInvitedVisitorsForAdmins(Long companyId, int timeFormat) {
        log.debug("Request to get all Visitants");
        List<VisitantInvitation> result = visitantInvitationRepository.findByCompanyIdAndStatusAndHasscheduleOrCompanyIdAndStatusAndHasschedule(companyId, 1, timeFormat, companyId, 2, timeFormat);

        List<VisitantInvitation> result1 = new ArrayList<>();

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getHouseId() == null) {
                result1.add(result.get(i));
            }
        }

        Collections.reverse(result1);
        return new PageImpl<VisitantInvitation>(result1).map(visitant -> visitantInvitationMapper.toDto(visitant));
    }

    @Transactional(readOnly = true)
    public List<VisitantInvitationDTO> getByMacroWithIdentification(Long macroId, String identificationnumber) {
        log.debug("Request to get all Residents");
        List<VisitantInvitation> result;
        List<Long> companiesId = new ArrayList<>();
        macroCondominiumService.findOne(macroId).getCompanies().forEach(companyDTO -> {
            companiesId.add(companyDTO.getId());
        });
        result = visitantInvitationRepository.findByStatusAndIdentificationnumberAndCompanyIdIn(1, identificationnumber, companiesId);
        List<VisitantInvitationDTO> result1 = new ArrayList<>();
        result.forEach(visitantInvitation -> {
            VisitantInvitation vs = verifyIfVisitantInvitationIsActive(visitantInvitation);
            if(vs!=null) {
                result1.add(visitantInvitationMapper.toDto(vs));
            }
        });
        return result1;
    }

    @Transactional(readOnly = true)
    public List<VisitantInvitationDTO> getByCompanyWithIdentification(Long companyId, String identificationnumber) {
        log.debug("Request to get all Residents");
        List<VisitantInvitation> result;

        result = visitantInvitationRepository.findByStatusAndIdentificationnumberAndCompanyId(1, identificationnumber, companyId);
        List<VisitantInvitationDTO> result1 = new ArrayList<>();
        result.forEach(visitantInvitation -> {
            VisitantInvitation vs = verifyIfVisitantInvitationIsActive(visitantInvitation);
            if(vs!=null) {
                result1.add(visitantInvitationMapper.toDto(vs));
            }
        });
        return result1;
    }
    @Transactional(readOnly = true)
    public List<VisitantInvitationDTO> getByMacroWithPlate(Long macroId, String plate) {
        log.debug("Request to get all Residents");
        List<VisitantInvitation> result;
        List<Long> companiesId = new ArrayList<>();
        macroCondominiumService.findOne(macroId).getCompanies().forEach(companyDTO -> {
            companiesId.add(companyDTO.getId());
        });
        result = visitantInvitationRepository.findByStatusAndLicenseplateAndCompanyIdIn(1, plate, companiesId);
        List<VisitantInvitationDTO> result1 = new ArrayList<>();
        result.forEach(visitantInvitation -> {
            VisitantInvitation vs = verifyIfVisitantInvitationIsActive(visitantInvitation);
            if(vs!=null) {
                result1.add(visitantInvitationMapper.toDto(vs));
            }
        });
        return result1;
    }
    @Transactional(readOnly = true)
    public List<VisitantInvitationDTO> getByCompanyWithPlate(Long companyId, String plate) {
        log.debug("Request to get all Residents");
        List<VisitantInvitation> result;
        result = visitantInvitationRepository.findByStatusAndLicenseplateAndCompanyId(1, plate, companyId);
        List<VisitantInvitationDTO> result1 = new ArrayList<>();
        result.forEach(visitantInvitation -> {
            VisitantInvitation vs = verifyIfVisitantInvitationIsActive(visitantInvitation);
            if(vs!=null) {
                result1.add(visitantInvitationMapper.toDto(vs));
            }
        });
        return result1;
    }


    /**
     * Get all the visitantInvitations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VisitantInvitationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all VisitantInvitations");
        return visitantInvitationRepository.findAll(pageable)
            .map(visitantInvitationMapper::toDto);
    }

    /**
     * Get one visitantInvitation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public VisitantInvitationDTO findOne(Long id) {
        log.debug("Request to get VisitantInvitation : {}", id);
        VisitantInvitation visitantInvitation = visitantInvitationRepository.findOne(id);
        return visitantInvitationMapper.toDto(visitantInvitation);
    }

    @Transactional(readOnly = true)
    public VisitantInvitationDTO findInvitedVisitorByHouse(String identificationNumber, Long houseId, Long companyId, int hasSchedule) {
        log.debug("Request to find if there is already a registered visitor with this identification number : {}", identificationNumber);
        VisitantInvitation visitant = visitantInvitationRepository.findByIdentificationnumberAndHouseIdAndCompanyIdAndHasschedule(identificationNumber, houseId, companyId, hasSchedule);
        VisitantInvitationDTO visitantDTO = visitantInvitationMapper.toDto(visitant);
        return visitantDTO;
    }
    @Transactional(readOnly = true)
    public List<VisitantInvitationDTO> getActiveInvitedByCompany(Long companyId) {
        List<VisitantInvitation> visitant = visitantInvitationRepository.findByCompanyIdAndStatus(companyId, 1);
        List<VisitantInvitationDTO> filterList = new ArrayList<>();
        visitant.forEach(visitantInvitation -> {
            VisitantInvitation visitantActive =  this.verifyIfVisitantInvitationIsActive(visitantInvitation);
            if(visitantActive!=null){
                VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantActive);
                if(visitantInvitationDTO.getHouseId()!=null){
                    visitantInvitationDTO.setHouseNumber(this.houseService.findOne(visitantInvitationDTO.getHouseId()).getHousenumber());
                }else{
                    visitantInvitationDTO.setDestiny(visitantInvitation.getDestiny());
                }
                filterList.add(visitantInvitationDTO);
            }
        });
        return filterList;
    }

    @Transactional(readOnly = true)
    public List<VisitantInvitationDTO> getActiveInvitedByHouse(Long houseId) {
        List<VisitantInvitation> visitant = visitantInvitationRepository.findByHouseIdAndStatus(houseId, 1);
        List<VisitantInvitationDTO> filterList = new ArrayList<>();
        visitant.forEach(visitantInvitation -> {
            VisitantInvitation visitantActive =  this.verifyIfVisitantInvitationIsActive(visitantInvitation);
            if(visitantActive!=null){
                VisitantInvitationDTO visitantInvitationDTO = visitantInvitationMapper.toDto(visitantActive);
                if(visitantInvitationDTO.getHouseId()!=null){
                    visitantInvitationDTO.setHouseNumber(this.houseService.findOne(visitantInvitationDTO.getHouseId()).getHousenumber());
                }else{
                    visitantInvitationDTO.setDestiny(visitantInvitation.getDestiny());
                }
                filterList.add(visitantInvitationDTO);
            }
        });
        return filterList;
    }
    /**
     * Delete the visitantInvitation by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        VisitantInvitationDTO temporalVisitant = this.findOne(id);
        log.debug("Request to delete VisitantInvitation : {}", id);
        visitantInvitationRepository.delete(id);
        if (temporalVisitant.getHasschedule() == 1) {
            invitationScheduleService.delete(invitationScheduleService.findSchedulesByInvitation(id).get(0).getId());
        }
    }

    public VisitantInvitation verifyIfVisitantInvitationIsActive(VisitantInvitation visitantInvitation) {
        if (visitantInvitation.getHasschedule() == 0) {
            if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                return visitantInvitation;
            }
        } else {
            return verifyInSchedule(visitantInvitation);
        }
        return null;
    }

    private boolean isNowBetweenHours(ZonedDateTime initialT, ZonedDateTime finalT) {
        ZonedDateTime now = ZonedDateTime.now();
        if (initialT.isBefore(now) && finalT.isAfter(now)) {
            return true;
        }
        return false;
    }

    private VisitantInvitation buildZonedDateTime(VisitantInvitation visitantInvitation,String stringDate) {
        String initS = stringDate.split("-")[0];
        String finalS = stringDate.split("-")[1];
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime initialZ = now.withHour(Integer.parseInt(initS.split(":")[0])).withMinute(Integer.parseInt(initS.split(":")[1])).withSecond(0).withNano(0);
        ZonedDateTime finalZ = now.withHour(Integer.parseInt(finalS.split(":")[0])).withMinute(Integer.parseInt(finalS.split(":")[1])).withSecond(0).withNano(0);
        visitantInvitation.setInvitationstartingtime(initialZ);
        visitantInvitation.setInvitationlimittime(finalZ);
       return visitantInvitation;
    }

    private VisitantInvitation verifyInSchedule(VisitantInvitation visitantInvitation) {
        InvitationScheduleDTO schedule = this.invitationScheduleService.findOneSchedulesByInvitation(visitantInvitation.getId());
        ZonedDateTime now = ZonedDateTime.now();
        Integer day = now.getDayOfWeek().getValue();
        switch (day) {
            case 1:
                if(schedule.getLunes()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getLunes());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
            case 2:
                if(schedule.getMartes()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getMartes());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
            case 3:
                if(schedule.getMiercoles()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getMiercoles());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
            case 4:
                if(schedule.getJueves()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getJueves());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
            case 5:
                if(schedule.getViernes()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getViernes());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
            case 6:
                if(schedule.getSabado()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getSabado());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
            case 7:
                if(schedule.getDomingo()!=null){
                    visitantInvitation = buildZonedDateTime(visitantInvitation,schedule.getDomingo());
                    if (isNowBetweenHours(visitantInvitation.getInvitationstartingtime(),visitantInvitation.getInvitationlimittime())) {
                        return visitantInvitation;
                    }
                }
                break;
        }
        return null;
    }
}
