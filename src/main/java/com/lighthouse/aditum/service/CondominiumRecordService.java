package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.CondominiumRecord;
import com.lighthouse.aditum.repository.CondominiumRecordRepository;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.CondominiumRecordDTO;
import com.lighthouse.aditum.service.mapper.CondominiumRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;


/**
 * Service Implementation for managing CondominiumRecord.
 */
@Service
@Transactional
public class CondominiumRecordService {

    private final Logger log = LoggerFactory.getLogger(CondominiumRecordService.class);

    private final CondominiumRecordRepository condominiumRecordRepository;

    private final CondominiumRecordMapper condominiumRecordMapper;

    private final PushNotificationService pNotification;

    private final CompanyService companyService;

    public CondominiumRecordService(CompanyService companyService, PushNotificationService pNotification, CondominiumRecordRepository condominiumRecordRepository, CondominiumRecordMapper condominiumRecordMapper) {
        this.condominiumRecordRepository = condominiumRecordRepository;
        this.condominiumRecordMapper = condominiumRecordMapper;
        this.companyService = companyService;
        this.pNotification = pNotification;
    }

    /**
     * Save a condominiumRecord.
     *
     * @param condominiumRecordDTO the entity to save
     * @return the persisted entity
     */
    public CondominiumRecordDTO save(CondominiumRecordDTO condominiumRecordDTO) throws URISyntaxException {
        log.debug("Request to save CondominiumRecord : {}", condominiumRecordDTO);
        CondominiumRecord condominiumRecord = condominiumRecordMapper.toEntity(condominiumRecordDTO);
        condominiumRecord = condominiumRecordRepository.save(condominiumRecord);
        CompanyDTO company = this.companyService.findOne(condominiumRecordDTO.getCompanyId());
        if (condominiumRecordDTO.getDeleted() == 0 && condominiumRecordDTO.getId() == null) {
            String bodyNoti = "";
            String bodyNotiAdmin = "";
            if (condominiumRecordDTO.getStatus() == 1) {
                bodyNoti = "Se ha publicado una nueva acta condominal, ingrese para ver más detalles.";
                bodyNotiAdmin = "Se ha publicado una nueva acta condominal.";
            } else {
                bodyNoti = "Se ha publicado una minuta del condominio, ingrese para ver más detalles.";
                bodyNotiAdmin = "Se ha publicado una nueva minuta del condominio.";
            }
            this.pNotification.sendNotificationsToAllResidentsByCompany(condominiumRecordDTO.getCompanyId(), this.pNotification.createPushNotification(
                condominiumRecordDTO.getName() + " - " + company.getName(),
                bodyNoti
            ));
            this.pNotification.sendNotificationAllAdminsByCompanyId(condominiumRecordDTO.getCompanyId(), this.pNotification.createPushNotification(
                condominiumRecordDTO.getName() + " - " + company.getName(),
                bodyNotiAdmin
            ));
        }
        return condominiumRecordMapper.toDto(condominiumRecord);
    }

    /**
     * Get all the condominiumRecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CondominiumRecordDTO> findAll(Pageable pageable, Long companyId, int type) {
        log.debug("Request to get all CondominiumRecords");
        return condominiumRecordRepository.findAllByCompanyIdAndDeletedAndStatus(pageable, companyId, 0, type)
            .map(condominiumRecordMapper::toDto);
    }

    /**
     * Get one condominiumRecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public CondominiumRecordDTO findOne(Long id) {
        log.debug("Request to get CondominiumRecord : {}", id);
        CondominiumRecord condominiumRecord = condominiumRecordRepository.findOne(id);
        return condominiumRecordMapper.toDto(condominiumRecord);
    }

    /**
     * Delete the condominiumRecord by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CondominiumRecord : {}", id);
        condominiumRecordRepository.delete(id);
    }
}
