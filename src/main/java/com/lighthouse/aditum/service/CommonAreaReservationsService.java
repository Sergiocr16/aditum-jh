package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.repository.CommonAreaReservationsRepository;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import com.lighthouse.aditum.service.dto.CommonAreaDTO;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaReservationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Service Implementation for managing CommonAreaReservations.
 */
@Service
@Transactional
public class CommonAreaReservationsService {

    private final Logger log = LoggerFactory.getLogger(CommonAreaReservationsService.class);

    private final CommonAreaReservationsRepository commonAreaReservationsRepository;

    private final ResidentService residentService;

    private final HouseService houseService;

    private final CommonAreaService commonAreaService;

    private final CommonAreaMailService commonAreaMailService;

    private final ChargeService chargeService;

    private final CommonAreaReservationsMapper commonAreaReservationsMapper;

    public CommonAreaReservationsService(CommonAreaReservationsRepository commonAreaReservationsRepository, CommonAreaReservationsMapper commonAreaReservationsMapper, HouseService houseService, ResidentService residentService,CommonAreaMailService commonAreaMailService,CommonAreaService commonAreaService,ChargeService chargeService) {
        this.commonAreaReservationsRepository = commonAreaReservationsRepository;
        this.commonAreaReservationsMapper = commonAreaReservationsMapper;
        this.houseService = houseService;
        this.residentService = residentService;
        this.commonAreaMailService = commonAreaMailService;
        this.commonAreaService = commonAreaService;
        this.chargeService = chargeService;
    }

    /**
     * Save a commonAreaReservations.
     *
     * @param commonAreaReservationsDTO the entity to save
     * @return the persisted entity
     */
    public CommonAreaReservationsDTO save(CommonAreaReservationsDTO commonAreaReservationsDTO) {
        log.debug("Request to save CommonAreaReservations : {}", commonAreaReservationsDTO);

        commonAreaReservationsDTO.setFinalDate(commonAreaReservationsDTO.getInitalDate().plusHours(Integer.parseInt(commonAreaReservationsDTO.getFinalTime())));
        commonAreaReservationsDTO.setInitalDate(commonAreaReservationsDTO.getInitalDate().plusHours(Integer.parseInt(commonAreaReservationsDTO.getInitialTime())));
        CommonAreaReservations commonAreaReservations = commonAreaReservationsMapper.toEntity(commonAreaReservationsDTO);
        commonAreaReservations.setChargeEmail(commonAreaReservationsDTO.getChargeEmail());
        commonAreaReservations = commonAreaReservationsRepository.save(commonAreaReservations);
        CommonAreaReservationsDTO commonAreaReservationsDTO1 = commonAreaReservationsMapper.toDto(commonAreaReservations);
        commonAreaReservationsDTO1.setChargeEmail(commonAreaReservations.getChargeEmail());
        commonAreaReservationsDTO1.setResident(residentService.findOne(commonAreaReservationsDTO1.getResidentId()));
        commonAreaReservationsDTO1.setHouse(houseService.findOne(commonAreaReservationsDTO1.getHouseId()));
        if(commonAreaReservationsDTO1.getChargeIdId()!=null){
            commonAreaReservationsDTO1.setCharge(chargeService.findOne(commonAreaReservationsDTO1.getChargeIdId()));
        }
        commonAreaReservationsDTO1.setCommonArea(commonAreaService.findOne(commonAreaReservationsDTO1.getCommonAreaId()));

        if(commonAreaReservationsDTO.getId()==null){
            this.commonAreaMailService.sendNewCommonAreaReservationEmailToResident(commonAreaReservationsDTO1);
            if(commonAreaReservationsDTO.isSendPendingEmail()){
                this.commonAreaMailService.sendNewCommonAreaReservationEmailToAdmin(commonAreaReservationsDTO1);
            }

        }else if(commonAreaReservationsDTO.getId()!=null && commonAreaReservationsDTO.isSendPendingEmail() && commonAreaReservations.getStatus()==1){
            this.commonAreaMailService.sendUpdateCommonAreaReservationEmail(commonAreaReservationsDTO1);
        }else if(commonAreaReservationsDTO.getId()!=null && commonAreaReservationsDTO.isSendPendingEmail() && commonAreaReservations.getStatus()==2){
            commonAreaReservationsDTO1.getResident().setEmail(commonAreaReservationsDTO.getResident().getEmail());
            this.commonAreaMailService.sendAcceptedCommonAreaReservationEmail(commonAreaReservationsDTO1);
        }else if(commonAreaReservationsDTO.getId()!=null && commonAreaReservationsDTO.isSendPendingEmail() && commonAreaReservations.getStatus()==3){
            this.commonAreaMailService.sendCanceledCommonAreaReservationEmail(commonAreaReservationsDTO1);
        }

        return commonAreaReservationsDTO1;

    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findByDatesBetweenAndCompany(Pageable pageable,String initialTime,String finalTime,Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        Page<CommonAreaReservations> result = commonAreaReservationsRepository.findByDatesBetweenAndCompany(pageable,zd_initialTime,zd_finalTime,companyId);
        return result.map(commonAreaReservations -> commonAreaReservationsMapper.toDto(commonAreaReservations));
    }

    @Transactional(readOnly = true)
    public  CommonAreaReservationsDTO isAvailableToReserve(int maximun_hours,String reservation_date,String initial_time,String final_time, Long common_area_id,Long reservation_id) {
        CommonAreaReservationsDTO commonAreaReservationsDTO = new CommonAreaReservationsDTO();

        if(maximun_hours==0){
            ZonedDateTime zd_reservation_initial_date = ZonedDateTime.parse(reservation_date+"[America/Regina]");
            ZonedDateTime zd_reservation_final_date = ZonedDateTime.parse((reservation_date+"[America/Regina]").replace("00:00:00","23:59:59"));
            List<CommonAreaReservations> reservations = commonAreaReservationsRepository.findByBetweenDatesAndCommonArea(zd_reservation_initial_date,zd_reservation_final_date,common_area_id);
            if(reservations.size()>0){
                commonAreaReservationsDTO.setAvailable(false);
            }else{
                commonAreaReservationsDTO.setAvailable(true);
            }
        }else{

            ZonedDateTime zd_reservation_initial_date = ZonedDateTime.parse(reservation_date+"[America/Regina]");
            ZonedDateTime zd_reservation_final_date = ZonedDateTime.parse((reservation_date+"[America/Regina]"));
            zd_reservation_initial_date = zd_reservation_initial_date.plusHours(Integer.parseInt(initial_time));
            zd_reservation_final_date = zd_reservation_final_date.plusHours(Integer.parseInt(final_time));
            List<CommonAreaReservations> test1 = commonAreaReservationsRepository.findReservationBetweenIT(zd_reservation_initial_date,zd_reservation_final_date,common_area_id);
            List<CommonAreaReservations> test2 = commonAreaReservationsRepository.findReservationBetweenFT(zd_reservation_initial_date,zd_reservation_final_date,common_area_id);
            List<CommonAreaReservations> test3 = commonAreaReservationsRepository. findReservationInIT(zd_reservation_initial_date,common_area_id);
            List<CommonAreaReservations> allReservations = new ArrayList<>();
            allReservations.addAll(test1);
            allReservations.addAll(test2);
            allReservations.addAll(test3);
            int cantidad = 0;

            for(int i = 0; i < allReservations.size(); i++) {
                if(allReservations.get(i).getId()!=reservation_id){
                    cantidad++;
                };
            }
            if(allReservations.size()>0 && cantidad>0){

                commonAreaReservationsDTO.setAvailable(false);
            }else{
                commonAreaReservationsDTO.setAvailable(true);
            }
        }
        return commonAreaReservationsDTO;
    }
    /**
     *  Get all the commonAreaReservations.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findAll(Pageable pageable,Long companyId) {
        log.debug("Request to get all CommonAreaReservations");

        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByCompanyIdAndStatusNot(pageable,companyId,4).map(commonAreaReservationsMapper::toDto);

        return mapCommonAreaReservations(commonAreaReservationsDTOPage);
    }
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getPendingReservations(Pageable pageable,Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage =  commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable,companyId,1).map(commonAreaReservationsMapper::toDto);

        return mapCommonAreaReservations(commonAreaReservationsDTOPage);

    }

    private Page<CommonAreaReservationsDTO> mapCommonAreaReservations(Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage) {
        for (int i = 0; i < commonAreaReservationsDTOPage.getContent().size(); i++) {
            commonAreaReservationsDTOPage.getContent().get(i).setHouseNumber(houseService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getHouseId()).getHousenumber());
            CommonAreaDTO commonAreaDTO = commonAreaService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getCommonAreaId());
            commonAreaReservationsDTOPage.getContent().get(i).setCommonAreaName(commonAreaDTO.getName());
            commonAreaReservationsDTOPage.getContent().get(i).setCommonAreaPicture(commonAreaDTO.getPicture());
            commonAreaReservationsDTOPage.getContent().get(i).setCommonAreapictureContentType(commonAreaDTO.getPictureContentType());
            ResidentDTO residentDTO = residentService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getResidentId());
            commonAreaReservationsDTOPage.getContent().get(i).setResidentName(residentDTO.getName() + " " + residentDTO.getLastname());
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            if(zonedDateTime.isAfter(commonAreaReservationsDTOPage.getContent().get(i).getFinalDate()) && commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId()==null || zonedDateTime.isAfter(commonAreaReservationsDTOPage.getContent().get(i).getFinalDate()) && commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId()!=null && commonAreaReservationsDTOPage.getContent().get(i).getDevolutionAmmount()==0){
                commonAreaReservationsDTOPage.getContent().get(i).setStatus(5);
            }
        }
        return commonAreaReservationsDTOPage;
    }

    @Transactional(readOnly = true)
    public List<CommonAreaReservationsDTO> getAcceptedReservations(Pageable pageable,Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage =  commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable,companyId,2).map(commonAreaReservationsMapper::toDto);
        commonAreaReservationsDTOPage = mapCommonAreaReservations(commonAreaReservationsDTOPage);
        List<CommonAreaReservationsDTO> finalList = new ArrayList<>();
        for (int i = 0; i < commonAreaReservationsDTOPage.getContent().size(); i++) {
            ChargeDTO chargeDTO = chargeService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId());
          if(commonAreaReservationsDTOPage.getContent().get(i).getStatus()!=5 && commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId()!=null && commonAreaReservationsDTOPage.getContent().get(i).getDevolutionAmmount()>0 && chargeDTO.getState()==2){
              finalList.add(commonAreaReservationsDTOPage.getContent().get(i));
          }
        }
        return finalList;

    }
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getPendingAndAcceptedReservations(Pageable pageable,Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findgetPendingAndAcceptedReservations(pageable,companyId)
            .map(commonAreaReservationsMapper::toDto);

    }
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getReservationsByCommonArea(Pageable pageable,Long commonAreaId) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findByCommonAreaIdAndStatus(pageable,commonAreaId)
            .map(commonAreaReservationsMapper::toDto);

    }
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findByHouseId(Pageable pageable,Long houseId) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findByHouseIdAndStatusNot(pageable,houseId,4)
            .map(commonAreaReservationsMapper::toDto);

    }

    /**
     *  Get one commonAreaReservations by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public CommonAreaReservationsDTO findOne(Long id) {
        log.debug("Request to get CommonAreaReservations : {}", id);
        CommonAreaReservations commonAreaReservations = commonAreaReservationsRepository.findOne(id);
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
        commonAreaReservationsDTO.setChargeEmail(commonAreaReservations.getChargeEmail());
        commonAreaReservationsDTO.setResident(residentService.findOne(commonAreaReservationsDTO.getResidentId()));
        return commonAreaReservationsDTO ;
    }

    /**
     *  Delete the  commonAreaReservations by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonAreaReservations : {}", id);
        commonAreaReservationsRepository.delete(id);
    }
}
