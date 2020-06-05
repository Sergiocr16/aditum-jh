package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CommonArea;
import com.lighthouse.aditum.domain.CommonAreaReservations;
import com.lighthouse.aditum.domain.ReservationHouseRestrictions;
import com.lighthouse.aditum.repository.CommonAreaReservationsRepository;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.service.mapper.CommonAreaReservationsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.LongAccumulator;

import static com.lighthouse.aditum.service.util.RandomUtil.createBitacoraAcciones;


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

    private final EgressService egressService;

    private final PaymentService paymentService;

    private final ReservationHouseRestrictionsService reservationHouseRestrictionsService;

    private final BitacoraAccionesService bitacoraAccionesService;


    private final CommonAreaReservationsMapper commonAreaReservationsMapper;

    public CommonAreaReservationsService(ReservationHouseRestrictionsService reservationHouseRestrictionsService, BitacoraAccionesService bitacoraAccionesService, CommonAreaReservationsRepository commonAreaReservationsRepository, CommonAreaReservationsMapper commonAreaReservationsMapper, HouseService houseService, ResidentService residentService, CommonAreaMailService commonAreaMailService, CommonAreaService commonAreaService, ChargeService chargeService, EgressService egressService, PaymentService paymentService) {
        this.commonAreaReservationsRepository = commonAreaReservationsRepository;
        this.commonAreaReservationsMapper = commonAreaReservationsMapper;
        this.houseService = houseService;
        this.residentService = residentService;
        this.commonAreaMailService = commonAreaMailService;
        this.commonAreaService = commonAreaService;
        this.chargeService = chargeService;
        this.egressService = egressService;
        this.paymentService = paymentService;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.reservationHouseRestrictionsService = reservationHouseRestrictionsService;
    }

    /**
     * Save a commonAreaReservations.
     *
     * @param commonAreaReservationsDTO the entity to save
     * @return the persisted entity
     */
    public CommonAreaReservationsDTO save(CommonAreaReservationsDTO commonAreaReservationsDTO) throws URISyntaxException {
        log.debug("Request to save CommonAreaReservations : {}", commonAreaReservationsDTO);
        commonAreaReservationsDTO.setFinalDate(commonAreaReservationsDTO.getInitalDate().plusHours(Integer.parseInt(commonAreaReservationsDTO.getFinalTime())));
        commonAreaReservationsDTO.setInitalDate(commonAreaReservationsDTO.getInitalDate().plusHours(Integer.parseInt(commonAreaReservationsDTO.getInitialTime())));
        CommonAreaReservations commonAreaReservations = commonAreaReservationsMapper.toEntity(commonAreaReservationsDTO);
        commonAreaReservations.setChargeEmail(commonAreaReservationsDTO.getChargeEmail());
        commonAreaReservations.setEgressId(commonAreaReservationsDTO.getEgressId());
        CommonAreaDTO commonAreaDTO = this.commonAreaService.findOne(commonAreaReservationsDTO.getCommonAreaId());
        commonAreaReservationsDTO.setSendPendingEmail(true);
        if (commonAreaDTO.getNeedsApproval() == 0 && (commonAreaReservations.getStatus() != 10 || commonAreaReservations.getStatus() != 11)) {
//            commonAreaReservations.setStatus(2);
            commonAreaReservationsDTO.setSendPendingEmail(true);
        }

        if (commonAreaReservationsDTO.getStatus() == 1) {
            this.reservationHouseRestrictionsService.increaseQuantity(commonAreaReservationsDTO.getHouseId(), commonAreaReservationsDTO.getCommonAreaId(), commonAreaReservationsDTO.getInitalDate());
        }
        if (commonAreaReservations.getStatus() == 3 || commonAreaReservations.getStatus() == 10 || commonAreaReservations.getStatus() == 11) {
            this.reservationHouseRestrictionsService.decreaseQuantity(commonAreaReservationsDTO.getHouseId(), commonAreaReservationsDTO.getCommonAreaId());
        }
        commonAreaReservations = commonAreaReservationsRepository.save(commonAreaReservations);
        CommonAreaReservationsDTO commonAreaReservationsDTO1 = commonAreaReservationsMapper.toDto(commonAreaReservations);
        commonAreaReservationsDTO1.setChargeEmail(commonAreaReservations.getChargeEmail());
        commonAreaReservationsDTO1.setResident(residentService.findOne(commonAreaReservationsDTO1.getResidentId()));
        commonAreaReservationsDTO1.setHouse(houseService.findOne(commonAreaReservationsDTO1.getHouseId()));
        if (commonAreaReservationsDTO1.getChargeIdId() != null) {
            commonAreaReservationsDTO1.setCharge(chargeService.findOne(commonAreaReservationsDTO1.getChargeIdId()));
        }
        commonAreaReservationsDTO1.setCommonArea(commonAreaService.findOne(commonAreaReservationsDTO1.getCommonAreaId()));
        String concepto = "";
        String url = "";
        boolean saveBitacora = true;

        if (commonAreaReservationsDTO.getId() == null) {
            if (commonAreaDTO.getNeedsApproval() == 1) {
//                if (commonAreaReservationsDTO.isSendPendingEmail()) {
                    this.commonAreaMailService.sendNewCommonAreaReservationEmailToResident(commonAreaReservationsDTO1);
                    this.commonAreaMailService.sendNewCommonAreaReservationEmailToAdmin(commonAreaReservationsDTO1);
//                }
                concepto = "Solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
                saveBitacora = true;
                url = "common-area-administration.reservationDetail";
            } else {
//                if (commonAreaReservationsDTO.isSendPendingEmail()) {
                    this.commonAreaMailService.sendAcceptedCommonAreaReservationEmail(commonAreaReservationsDTO1);
//                }
                concepto = "Aprobación de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
                saveBitacora = true;
                url = "common-area-administration.acceptedReservationsDetail";
            }
        } else {
            switch (commonAreaReservationsDTO.getStatus()) {
                case 2:
                    commonAreaReservationsDTO1.getResident().setEmail(commonAreaReservationsDTO.getResident().getEmail());
//                    if (commonAreaReservationsDTO.isSendPendingEmail()) {
                        this.commonAreaMailService.sendAcceptedCommonAreaReservationEmail(commonAreaReservationsDTO1);
//                    }
                    concepto = "Aprobación de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
                    saveBitacora = true;
                    url = "common-area-administration.acceptedReservationsDetail";
                    break;
                case 3:
//                    if (commonAreaReservationsDTO.isSendPendingEmail()) {
                        this.commonAreaMailService.sendCanceledCommonAreaReservationEmail(commonAreaReservationsDTO1);
//                    }
                    concepto = "Rechazo de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
                    saveBitacora = true;
                    break;
                case 11:
//                    if (commonAreaReservationsDTO.isSendPendingEmail()) {
                        this.commonAreaMailService.sendCanceledCommonAreaReservationAprobedEmail(commonAreaReservationsDTO1);
//                    }
                    concepto = "Cancelación de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
                    saveBitacora = true;
                    break;
                case 10:
                    concepto = "Rechazo de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
                    saveBitacora = true;
                    break;
            }
        }
//
//
//        if (commonAreaReservationsDTO.getId() == null && commonAreaReservationsDTO.isSendPendingEmail() == false) {
//            concepto = "Solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//            url = "common-area-administration.reservationDetail";
//        } else if (commonAreaReservationsDTO.getId() == null && commonAreaReservationsDTO.isSendPendingEmail()) {
//            this.commonAreaMailService.sendNewCommonAreaReservationEmailToResident(commonAreaReservationsDTO1);
//            this.commonAreaMailService.sendNewCommonAreaReservationEmailToAdmin(commonAreaReservationsDTO1);
//
//            concepto = "Solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//            url = "common-area-administration.reservationDetail";
//        } else if (commonAreaReservationsDTO.getId() != null && commonAreaReservationsDTO.isSendPendingEmail() && commonAreaReservations.getStatus() == 2) {
//            commonAreaReservationsDTO1.getResident().setEmail(commonAreaReservationsDTO.getResident().getEmail());
//            this.commonAreaMailService.sendAcceptedCommonAreaReservationEmail(commonAreaReservationsDTO1);
//            concepto = "Aprobación de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//            url = "common-area-administration.acceptedReservationsDetail";
//        } else if (commonAreaReservationsDTO.getId() != null && commonAreaReservationsDTO.isSendPendingEmail() && commonAreaReservations.getStatus() == 3) {
//            this.commonAreaMailService.sendCanceledCommonAreaReservationEmail(commonAreaReservationsDTO1);
//            concepto = "Rechazo de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//        } else if (commonAreaReservationsDTO.getId() != null && commonAreaReservationsDTO.isSendPendingEmail() && commonAreaReservations.getStatus() == 11) {
//            this.commonAreaMailService.sendCanceledCommonAreaReservationAprobedEmail(commonAreaReservationsDTO1);
//            concepto = "Cancelación de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//        } else if (commonAreaReservationsDTO.getId() != null & commonAreaReservations.getStatus() == 2) {
//            concepto = "Aprobación de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//            url = "common-area-administration.acceptedReservationsDetail";
//        } else if (commonAreaReservationsDTO.getId() != null && commonAreaReservations.getStatus() == 10) {
//            concepto = "Rechazo de solicitud de reservación del área común: " + commonAreaReservationsDTO1.getCommonArea().getName();
//            saveBitacora = true;
//        }

        if (saveBitacora) {
            bitacoraAccionesService.save(createBitacoraAcciones(concepto, 11, url, "Reservaciones", commonAreaReservationsDTO1.getId(), commonAreaReservationsDTO1.getCompanyId(), commonAreaReservationsDTO1.getHouse().getId()));
        }
        return commonAreaReservationsDTO1;

    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findByDatesBetweenAndCompany(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<CommonAreaReservations> result = commonAreaReservationsRepository.findByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId);
        return mapCommonAreaReservations(result.map(commonAreaReservations -> commonAreaReservationsMapper.toDto(commonAreaReservations)));
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> forAccessDoor(Pageable pageable, ZonedDateTime initialTime, Long companyId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        Page<CommonAreaReservations> result = commonAreaReservationsRepository.findForAccessDoor(pageable, zd_initialTime, companyId);
        return mapCommonAreaReservations(result.map(commonAreaReservations -> commonAreaReservationsMapper.toDto(commonAreaReservations)));
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findByDatesBetweenAndCommonArea(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, Long commonAreaId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<CommonAreaReservations> result = commonAreaReservationsRepository.findByDatesBetweenAndCommonAreaId(pageable, zd_initialTime, zd_finalTime, commonAreaId);
        return mapCommonAreaReservations(result.map(commonAreaReservations -> commonAreaReservationsMapper.toDto(commonAreaReservations)));
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findByDatesBetweenAndHouse(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, Long houseId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<CommonAreaReservations> result = commonAreaReservationsRepository.findByDatesBetweenAndHouse(pageable, zd_initialTime, zd_finalTime, houseId);
        return mapCommonAreaReservations(result.map(commonAreaReservations -> commonAreaReservationsMapper.toDto(commonAreaReservations)));
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findDevolutionDoneByDatesBetweenAndCompany(Pageable pageable, ZonedDateTime initialTime, ZonedDateTime finalTime, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = initialTime.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_finalTime = finalTime.withMinute(59).withHour(23).withSecond(59);
        Page<CommonAreaReservations> commonAreaReservations = commonAreaReservationsRepository.findDevolutionDoneByDatesBetweenAndCompany(pageable, zd_initialTime, zd_finalTime, companyId, 6);
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservations.map(commonAreaReservationsMapper::toDto);
        commonAreaReservationsDTOPage = mapCommonAreaReservations(commonAreaReservationsDTOPage);
        for (int i = 0; i < commonAreaReservationsDTOPage.getContent().size(); i++) {
            if (commonAreaReservationsDTOPage.getContent().get(i).getEgressId() != null) {
                EgressDTO egressDTO = egressService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getEgressId());
                commonAreaReservationsDTOPage.getContent().get(i).setEgress(egressDTO);
            }
        }
        return commonAreaReservationsDTOPage;
    }

    @Transactional(readOnly = true)
    public int isAvailableToReserve(int maximun_hours, ZonedDateTime reservation_date, String initial_time, String final_time, Long common_area_id, Long houseId, Long reservation_id) {
        CommonAreaDTO commonArea = this.commonAreaService.findOne(common_area_id);
        int restrictions = this.isAbleTorReserveWithRestrictions(commonArea, houseId, reservation_date, initial_time, final_time);
        if (restrictions == 0) {
            if (maximun_hours == 0 && commonArea.getHasBlocks()==0) {
                ZonedDateTime zd_reservation_initial_date = reservation_date.withMinute(0).withHour(0).withSecond(0);
                ZonedDateTime zd_reservation_final_date = reservation_date.withMinute(59).withHour(23).withSecond(59);
                List<CommonAreaReservations> reservations = commonAreaReservationsRepository.findByBetweenDatesAndCommonArea(zd_reservation_initial_date, zd_reservation_final_date, common_area_id);
                if (reservations.size() < commonArea.getLimitPeoplePerReservation()) {
                    restrictions = 0;
                } else {
                    restrictions = 10;
                }
            } else {
                ZonedDateTime zd_reservation_initial_date = reservation_date.withMinute(0).withHour(0).withSecond(0);
                ZonedDateTime zd_reservation_final_date = reservation_date.withMinute(0).withHour(0).withSecond(0);
                zd_reservation_initial_date = zd_reservation_initial_date.plusHours(Integer.parseInt(initial_time));
                zd_reservation_final_date = zd_reservation_final_date.plusHours(Integer.parseInt(final_time));
                List<CommonAreaReservations> test1 = commonAreaReservationsRepository.findReservationBetweenIT(zd_reservation_initial_date, zd_reservation_final_date, common_area_id);
                List<CommonAreaReservations> test2 = commonAreaReservationsRepository.findReservationBetweenFT(zd_reservation_initial_date, zd_reservation_final_date, common_area_id);
                List<CommonAreaReservations> test3 = commonAreaReservationsRepository.findReservationInIT(zd_reservation_initial_date, common_area_id);
                List<CommonAreaReservations> allReservations = new ArrayList<>();
                allReservations.addAll(test1);
                allReservations.addAll(test2);
                allReservations.addAll(test3);
                int cantidad = 0;

                for (int i = 0; i < allReservations.size(); i++) {
                    if (allReservations.get(i).getId() != reservation_id) {
                        cantidad++;
                    }
                }
                if (allReservations.size() < commonArea.getLimitPeoplePerReservation() && cantidad < commonArea.getLimitPeoplePerReservation()) {
                    restrictions = 0;
                } else {
                    restrictions = 10;
                }
            }
        }
        return restrictions;
    }

    /**
     * Get all the commonAreaReservations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findAll(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");

        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByCompanyIdAndStatusNot(pageable, companyId, 4).map(
            commonAreaReservations -> {
                return mapCommonAreaReservation(commonAreaReservations);
            }
        );

        return commonAreaReservationsDTOPage;
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getPendingReservations(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable, companyId, 1).map(
            commonAreaReservations -> {
                return mapCommonAreaReservation(commonAreaReservations);
            }
        );
        return commonAreaReservationsDTOPage;
    }
    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getPendingReservationsDashboard(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable, companyId, 1).map(
            commonAreaReservations -> {
                return mapCommonAreaReservationDashboard(commonAreaReservations);
            }
        );
        return commonAreaReservationsDTOPage;
    }


    private Page<CommonAreaReservationsDTO> mapCommonAreaReservations(Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage) {

        commonAreaReservationsDTOPage.map(commonAreaReservation -> {
            commonAreaReservation.setHouse(houseService.findOne(commonAreaReservation.getHouseId()));
            commonAreaReservation.setPaymentProof(commonAreaReservation.getPaymentProof());
            commonAreaReservation.setResident(residentService.findOne(commonAreaReservation.getResidentId()));
            ZonedDateTime zonedDateTime = ZonedDateTime.now();
            if (zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getChargeIdId() == null && commonAreaReservation.getStatus() == 2 || zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getChargeIdId() != null && commonAreaReservation.getDevolutionAmmount() == 0 && commonAreaReservation.getStatus() == 2) {
                commonAreaReservation.setStatus(5);
            }

            if (zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getPaymentId() == null && commonAreaReservation.getStatus() == 2 && commonAreaReservation.getReservationCharge() > 0) {
                commonAreaReservation.setStatus(7);
            }
            ;
            return commonAreaReservation;
        });

        return commonAreaReservationsDTOPage;
    }

    private CommonAreaReservationsDTO mapCommonAreaReservation(CommonAreaReservations commonAreaReservations) {
        CommonAreaReservationsDTO commonAreaReservation = commonAreaReservationsMapper.toDto(commonAreaReservations);
        commonAreaReservation.setHouse(houseService.findOne(commonAreaReservation.getHouseId()));
        commonAreaReservation.setPaymentProof(commonAreaReservation.getPaymentProof());
        commonAreaReservation.setResident(residentService.findOne(commonAreaReservation.getResidentId()));
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        if (zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getChargeIdId() == null && commonAreaReservation.getStatus() == 2 || zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getChargeIdId() != null && commonAreaReservation.getDevolutionAmmount() == 0 && commonAreaReservation.getStatus() == 2) {
            commonAreaReservation.setStatus(5);
        }
        if (zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getPaymentId() == null && commonAreaReservation.getStatus() == 2 && commonAreaReservation.getReservationCharge() > 0) {
            commonAreaReservation.setStatus(7);
        }
        return commonAreaReservation;
    }
    private CommonAreaReservationsDTO mapCommonAreaReservationDashboard(CommonAreaReservations commonAreaReservations) {
        CommonAreaReservationsDTO commonAreaReservation = commonAreaReservationsMapper.toDto(commonAreaReservations);
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        if (zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getChargeIdId() == null && commonAreaReservation.getStatus() == 2 || zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getChargeIdId() != null && commonAreaReservation.getDevolutionAmmount() == 0 && commonAreaReservation.getStatus() == 2) {
            commonAreaReservation.setStatus(5);
        }
        if (zonedDateTime.isAfter(commonAreaReservation.getFinalDate()) && commonAreaReservation.getPaymentId() == null && commonAreaReservation.getStatus() == 2 && commonAreaReservation.getReservationCharge() > 0) {
            commonAreaReservation.setStatus(7);
        }
        return commonAreaReservation;
    }
    @Transactional(readOnly = true)
    public List<CommonAreaReservationsDTO> getAcceptedReservationsDashboard(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        List<CommonAreaReservationsDTO> finalList = new ArrayList<>();
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable, companyId, 2)
            .map(
                commonAreaReservations -> {
                    return mapCommonAreaReservationDashboard(commonAreaReservations);
                }
            );
        for (int i = 0; i < commonAreaReservationsDTOPage.getContent().size(); i++) {
            if (commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId() != null) {
                ChargeDTO chargeDTO = chargeService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId());
                if (commonAreaReservationsDTOPage.getContent().get(i).getStatus() != 5 && commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId() != null && commonAreaReservationsDTOPage.getContent().get(i).getDevolutionAmmount() > 0 && chargeDTO.getState() == 2) {
                    finalList.add(commonAreaReservationsDTOPage.getContent().get(i));
                }
            }
        }
        return finalList;
    }
    @Transactional(readOnly = true)
    public List<CommonAreaReservationsDTO> getAcceptedReservations(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        List<CommonAreaReservationsDTO> finalList = new ArrayList<>();
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable, companyId, 2)
            .map(
                commonAreaReservations -> {
                    return mapCommonAreaReservation(commonAreaReservations);
                }
            );
        for (int i = 0; i < commonAreaReservationsDTOPage.getContent().size(); i++) {
            if (commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId() != null) {
                ChargeDTO chargeDTO = chargeService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId());
                if (commonAreaReservationsDTOPage.getContent().get(i).getStatus() != 5 && commonAreaReservationsDTOPage.getContent().get(i).getChargeIdId() != null && commonAreaReservationsDTOPage.getContent().get(i).getDevolutionAmmount() > 0 && chargeDTO.getState() == 2) {
                    finalList.add(commonAreaReservationsDTOPage.getContent().get(i));
                }
            }
        }
        return finalList;
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getDevolutionDoneReservations(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        Page<CommonAreaReservations> commonAreaReservations = commonAreaReservationsRepository.findByCompanyIdAndStatus(pageable, companyId, 6);
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservations.map(commonAreaReservationsMapper::toDto);
        commonAreaReservationsDTOPage = mapCommonAreaReservations(commonAreaReservationsDTOPage);
        for (int i = 0; i < commonAreaReservationsDTOPage.getContent().size(); i++) {

            if (commonAreaReservationsDTOPage.getContent().get(i).getEgressId() != null) {
                EgressDTO egressDTO = egressService.findOne(commonAreaReservationsDTOPage.getContent().get(i).getEgressId());
                commonAreaReservationsDTOPage.getContent().get(i).setEgress(egressDTO);
            }
        }
        return commonAreaReservationsDTOPage;

    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getPendingAndAcceptedReservations(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findgetPendingAndAcceptedReservations(pageable, companyId)
            .map(commonAreaReservations -> {
                CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
                commonAreaReservationsDTO.setPaymentProof(commonAreaReservations.getPaymentProof());
                return this.hasValidityTime(commonAreaReservationsDTO);
            });
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getLastAcceptedReservations(Pageable pageable, Long companyId) {
        log.debug("Request to get all CommonAreaReservations");
        ZonedDateTime n = ZonedDateTime.now();
        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findTop5ByInitalDateAfterAndCompanyIdAndStatus(null, ZonedDateTime.now(), companyId, 2).map(commonAreaReservations -> {
            return mapCommonAreaReservation(commonAreaReservations);
        });
        return commonAreaReservationsDTOPage;
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> getReservationsByCommonArea(Pageable pageable, Long commonAreaId) {
        log.debug("Request to get all CommonAreaReservations");
        return commonAreaReservationsRepository.findByCommonAreaIdAndStatus(pageable, commonAreaId)
            .map(commonAreaReservations -> {
                CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
                commonAreaReservationsDTO.setPaymentProof(commonAreaReservations.getPaymentProof());
                return this.hasValidityTime(commonAreaReservationsDTO);
            });
    }

    @Transactional(readOnly = true)
    public Page<CommonAreaReservationsDTO> findByHouseId(Pageable pageable, Long houseId) {
        log.debug("Request to get all CommonAreaReservations");


        Page<CommonAreaReservationsDTO> commonAreaReservationsDTOPage = commonAreaReservationsRepository.findByHouseIdAndStatusNot(pageable, houseId, 4).map(
            commonAreaReservations -> {
                return mapCommonAreaReservation(commonAreaReservations);
            }
        );

        return commonAreaReservationsDTOPage;

//        return mapCommonAreaReservations(commonAreaReservationsRepository.findByHouseIdAndStatusNot(pageable, houseId, 4)
//            .map(commonAreaReservations -> {
//                CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
//                commonAreaReservationsDTO.setPaymentProof(commonAreaReservations.getPaymentProof());
//                return this.hasValidityTime(commonAreaReservationsDTO);
//            }));
    }

    @Transactional(readOnly = true)
    public CommonAreaReservationsDTO findByEgressId(Long egressId) {
        log.debug("Request to get all CommonAreaReservations");
        CommonAreaReservations commonAreaReservations = commonAreaReservationsRepository.findByEgressId(egressId);
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
        return this.hasValidityTime(commonAreaReservationsDTO);
    }

    /**
     * Get one commonAreaReservations by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CommonAreaReservationsDTO findOne(Long id) {
        log.debug("Request to get CommonAreaReservations : {}", id);
        CommonAreaReservations commonAreaReservations = commonAreaReservationsRepository.findOne(id);
        CommonAreaReservationsDTO commonAreaReservationsDTO = commonAreaReservationsMapper.toDto(commonAreaReservations);
        commonAreaReservationsDTO.setChargeEmail(commonAreaReservations.getChargeEmail());

        commonAreaReservationsDTO.setResident(residentService.findOne(commonAreaReservationsDTO.getResidentId()));
        if (commonAreaReservationsDTO.getEgressId() != null) {
            commonAreaReservationsDTO.setEgress(egressService.findOne(commonAreaReservationsDTO.getEgressId()));
        }
        if (commonAreaReservationsDTO.getChargeIdId() != null) {
            ChargeDTO chargeDTO = chargeService.findOne(commonAreaReservationsDTO.getChargeIdId());
            if (chargeDTO.getPaymentId() != null) {
                PaymentDTO paymentDTO = paymentService.findOne(chargeDTO.getPaymentId());
                commonAreaReservationsDTO.setPayment(paymentDTO);
                commonAreaReservationsDTO.setPaymentId(paymentDTO.getId());
            }
        }
        return this.hasValidityTime(commonAreaReservationsDTO);
    }

    public boolean isAbletoReserveHasDaysBeforeToReserve(CommonAreaDTO commonArea, ZonedDateTime fechaReserva) {
        if (commonArea.getHasDaysBeforeToReserve() == 1) {
            ZonedDateTime now = ZonedDateTime.now();
            Duration d = Duration.between(now, fechaReserva);
            int difDays = (int) d.toDays();
            if (difDays >= commonArea.getDaysBeforeToReserve()) {
                return true;
            }
        }
        return false;
    }

    public boolean isAbletoReserveHasDistanceBetweenReservations(CommonAreaDTO commonArea, Long houseId, ZonedDateTime fechaReserva) {
        if (commonArea.getHasDistanceBetweenReservations() == 1) {
            ReservationHouseRestrictionsDTO reservationHouseRestrictions = this.reservationHouseRestrictionsService.findRestrictionByHouseAndCommonArea(houseId, commonArea.getId());
            if (reservationHouseRestrictions != null) {
                ZonedDateTime lastTime = reservationHouseRestrictions.getLastTimeReservation();
                Period d = Period.between(lastTime.toLocalDate(), fechaReserva.toLocalDate());
                int diffMonths = d.getMonths();
                if (diffMonths >= commonArea.getDistanceBetweenReservations()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean isAbletoReserveWithActiveReservations(CommonAreaDTO commonArea, Long houseId, ZonedDateTime fechaReserva) {
        List<CommonAreaReservations> commonAreaReservationsList = this.commonAreaReservationsRepository.findByPendingAndAcceptedReservationsByHouseId(null,houseId).getContent();
        if (commonAreaReservationsList.size()>0) {
            int count = 0;
            for (CommonAreaReservations c: commonAreaReservationsList) {
                if(!ZonedDateTime.now().isAfter(c.getFinalDate())){
                    count++;
                }
            }
            if(count>=commonArea.getLimitActiveReservations()){
                return false;
            }
        }
        return true;
    }

    public boolean isAbleToReserveSameDay(CommonAreaDTO commonArea, Long houseId, ZonedDateTime fechaReserva) {
        ZonedDateTime zd_reservation_initial_date = fechaReserva.withMinute(0).withHour(0).withSecond(0);
        ZonedDateTime zd_reservation_final_date = fechaReserva.withMinute(59).withHour(23).withSecond(59);
        List<CommonAreaReservations> commonAreaReservationsList = this.commonAreaReservationsRepository.findByDatesAndPendingAndAcceptedReservationsByHouseIdAndCommonArea(null,zd_reservation_initial_date,zd_reservation_final_date,houseId,commonArea.getId()).getContent();
        if (commonAreaReservationsList.size()>0) {
            return false;
        }
        return true;
    }



    public boolean isAbletoReserveQuantityPerPeriod(CommonAreaDTO commonArea, Long houseId) {
        if (commonArea.getHasReservationsLimit() == 1) {
            ReservationHouseRestrictionsDTO reservationHouseRestrictions = this.reservationHouseRestrictionsService.findRestrictionByHouseAndCommonArea(houseId, commonArea.getId());
            if (reservationHouseRestrictions != null) {
                if (reservationHouseRestrictions.getReservationQuantity() < commonArea.getPeriodTimes()) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public CommonAreaReservationsDTO hasValidityTime(CommonAreaReservationsDTO commonAreaReservation) {
//        CommonAreaDTO commonArea = this.commonAreaService.findOne(commonAreaReservation.getCommonAreaId());
//        if (commonArea.getHasValidityTime() == 1) {
//            if (commonAreaReservation.getStatus() == 1) {
//                ZonedDateTime now = ZonedDateTime.now();
//                Duration d = Duration.between(now, commonAreaReservation.getInitalDate());
//                if (commonAreaReservation.getStatus() == 1) {
//                    if (d.toHours() <= commonArea.getValidityTimeHours()) {
//                        commonAreaReservation.setValidityTimePassed(commonArea.getValidityTimeHours());
//                    }
//                }
//            }
//        }
        return commonAreaReservation;
    }

    public boolean isAbleToReserveHasDaysToReserveIfFree(CommonArea commonArea, ZonedDateTime fechaReserva, String initialTime, String finalTime) {
        ZonedDateTime now = ZonedDateTime.now();
        if (commonArea.getHasDaysToReserveIfFree() == 1) {
            String[] daysBeforeToReserve = commonArea.getDaysToReserveIfFree().split("-");
            for (int i = Integer.parseInt(daysBeforeToReserve[0]); i <= Integer.parseInt(daysBeforeToReserve[1]); i++) {
                ZonedDateTime fechaReservaNdias = now.plusDays(i).withMinute(1).withHour(1).withNano(0).withSecond(0);
                if (fechaReservaNdias.equals(fechaReserva.withHour(1).withMinute(1).withNano(0).withSecond(0))) {
//                    CommonAreaReservationsDTO commonAreaReservationsDTO = this.isAvailableToReserve(commonArea.getMaximunHours(), fechaReservaNdias, initialTime, finalTime, commonArea.getId(), null);
//                    if (commonAreaReservationsDTO.isAvailable()==0) {
                    return true;
//                    }
                }
            }
        }
        return false;
    }

    public int isAbleTorReserveWithRestrictions(CommonAreaDTO commonArea, Long houseId, ZonedDateTime fechaReserva, String initialTime, String finalTime) {
        //       0 = Las restricciones se cumplen
        //       1 = No es posible porque ha llegado al limite de reservaciones por periodo
        //       2 = No es posible porque necesita reservar con n dias de antelacion
        //       3 = No es posible porque tiene distancias n meses entre reservaciones que no se han cumplido
        //       4 = No es posible porque tiene mas de n cantidad de reservas activas
        //       5 = No es posible porque ya ha reservado el día de hoy

        int state = 0;
        int state1 = (commonArea.getHasReservationsLimit() == 1 ? this.isAbletoReserveQuantityPerPeriod(commonArea, houseId) : true) ? 0 : 1;
        int state2 = (commonArea.getHasDaysBeforeToReserve() == 1 ? this.isAbletoReserveHasDaysBeforeToReserve(commonArea, fechaReserva) : true) ? 0 : 2;
        int state3 = (commonArea.getHasDistanceBetweenReservations() == 1 ? this.isAbletoReserveHasDistanceBetweenReservations(commonArea, houseId, fechaReserva) : true) ? 0 : 3;
        int state4 = this.isAbletoReserveWithActiveReservations(commonArea, houseId, fechaReserva)? 0 : 4;
        int state5 = this.isAbleToReserveSameDay(commonArea, houseId, fechaReserva)? 0 : 5;

        if (state1 == 0 && state2 == 0 && state3 == 0 && state4 == 0 && state5 == 0) {
            return 0;
        } else {
            if (state1 != 0) {
                return state1;
            }
            if (state2 != 0) {
                return state2;
            }
            if (state3 != 0) {
                return state3;
            }
            if (state4 != 0) {
                return state4;
            }
            if (state5 != 0) {
                return state5;
            }
        }
        return 0;
    }

    /**
     * Delete the  commonAreaReservations by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CommonAreaReservations : {}", id);
        commonAreaReservationsRepository.delete(id);
    }
}
