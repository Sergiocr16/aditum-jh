package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.NotificationSended;
import com.lighthouse.aditum.repository.NotificationSendedRepository;
import com.lighthouse.aditum.service.dto.NotificationRequestDTO;
import com.lighthouse.aditum.service.dto.NotificationSendedDTO;
import com.lighthouse.aditum.service.mapper.NotificationSendedMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;


/**
 * Service Implementation for managing NotificationSended.
 */
@Service
@Transactional
public class NotificationSendedService {

    private final Logger log = LoggerFactory.getLogger(NotificationSendedService.class);

    private final NotificationSendedRepository notificationSendedRepository;

    private final NotificationSendedMapper notificationSendedMapper;

    private final PushNotificationService pushNotificationService;

    public NotificationSendedService(PushNotificationService pushNotificationService,NotificationSendedRepository notificationSendedRepository, NotificationSendedMapper notificationSendedMapper) {
        this.notificationSendedRepository = notificationSendedRepository;
        this.notificationSendedMapper = notificationSendedMapper;
        this.pushNotificationService = pushNotificationService;
    }

    /**
     * Save a notificationSended.
     *
     * @param notificationSendedDTO the entity to save
     * @return the persisted entity
     */
    public NotificationSendedDTO save(NotificationSendedDTO notificationSendedDTO) throws URISyntaxException {
        log.debug("Request to save NotificationSended : {}", notificationSendedDTO);
        String sendedTo = "";
        notificationSendedDTO.setDate(ZonedDateTime.now());
        notificationSendedDTO.setTitle(notificationSendedDTO.getTitle());
        NotificationRequestDTO n =  this.pushNotificationService.createPushNotification(notificationSendedDTO.getTitle(),notificationSendedDTO.getDescription());
        if(Integer.parseInt(notificationSendedDTO.getToAll())==0){
            List<String> housesId = Arrays.asList(notificationSendedDTO.getSendedTo().split(";"));
            if(notificationSendedDTO.getSendToResident()!=null){
                List<String> residentInfo = Arrays.asList(notificationSendedDTO.getSendToResident().split(";"));
                String h = housesId.get(0);
                String id = h.split(",")[0];
                String houseNumber = h.split(",")[1];
                String idResident = residentInfo.get(0);
                String name = residentInfo.get(1);
                sendedTo = houseNumber+": "+name;
                this.pushNotificationService.sendNotificationToResident(Long.parseLong(idResident),n);
            }else{
                for (int i = 0; i < housesId.size(); i++) {
                    String h = housesId.get(i);
                    String id = h.split(",")[0];
                    String houseNumber = h.split(",")[1];
                    sendedTo = sendedTo + houseNumber +", ";
                    this.pushNotificationService.sendNotificationsToAllLivingInHouse(Long.parseLong(id),n);
                }
            }
        }else{
            this.pushNotificationService.sendNotificationsToAllResidentsByCompany(notificationSendedDTO.getCompanyId(),n);
            sendedTo = "TODAS LAS FILIALES";
        }
        notificationSendedDTO.setSendedTo(sendedTo);

        NotificationSended notificationSended = notificationSendedMapper.toEntity(notificationSendedDTO);
        notificationSended.setDate(ZonedDateTime.now());
        notificationSended = notificationSendedRepository.save(notificationSended);
        return notificationSendedMapper.toDto(notificationSended);
    }

    /**
     * Get all the notificationSendeds.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<NotificationSendedDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NotificationSendeds");
        return notificationSendedRepository.findAll(pageable)
            .map(notificationSendedMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NotificationSendedDTO> findAllByCompany(Pageable pageable,Long companyId) {
        log.debug("Request to get all NotificationSendeds");
        return notificationSendedRepository.findAllByCompanyId(pageable,companyId)
            .map(notificationSendedMapper::toDto);
    }

    /**
     * Get one notificationSended by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public NotificationSendedDTO findOne(Long id) {
        log.debug("Request to get NotificationSended : {}", id);
        NotificationSended notificationSended = notificationSendedRepository.findOne(id);
        return notificationSendedMapper.toDto(notificationSended);
    }

    /**
     * Delete the notificationSended by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete NotificationSended : {}", id);
        notificationSendedRepository.delete(id);
    }
}
