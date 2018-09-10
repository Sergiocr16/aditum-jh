package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.repository.CommonAreaReservationsRepository;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;
import com.lighthouse.aditum.service.mapper.CommonAreaReservationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * Service Implementation for managing CommonAreaReservations.
 */
@Service
@Transactional
public class CommonAreaReservationsService {

    private final Logger log = LoggerFactory.getLogger(CommonAreaReservationsService.class);

    private final CommonAreaReservationsRepository commonAreaReservationsRepository;

    private final CommonAreaReservationsMapper commonAreaReservationsMapper;

    public CommonAreaReservationsService(CommonAreaReservationsRepository commonAreaReservationsRepository, CommonAreaReservationsMapper commonAreaReservationsMapper) {
        this.commonAreaReservationsRepository = commonAreaReservationsRepository;
        this.commonAreaReservationsMapper = commonAreaReservationsMapper;
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
        commonAreaReservations = commonAreaReservationsRepository.save(commonAreaReservations);
        return commonAreaReservationsMapper.toDto(commonAreaReservations);
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
        return commonAreaReservationsRepository.findByCompanyIdAndStatusNot(pageable,companyId,4)
            .map(commonAreaReservationsMapper::toDto);
    }
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getPendingReservations(Pageable pageable,Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable,companyId,1)
            .map(commonAreaReservationsMapper::toDto);

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

        return commonAreaReservationsMapper.toDto(commonAreaReservations);
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
