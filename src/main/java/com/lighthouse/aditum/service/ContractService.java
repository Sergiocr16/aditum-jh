package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Contract;
import com.lighthouse.aditum.repository.ContractRepository;
import com.lighthouse.aditum.service.dto.CompanyDTO;
import com.lighthouse.aditum.service.dto.ContractDTO;
import com.lighthouse.aditum.service.mapper.ContractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;


/**
 * Service Implementation for managing Contract.
 */
@Service
@Transactional
public class ContractService {

    private final Logger log = LoggerFactory.getLogger(ContractService.class);

    private final ContractRepository contractRepository;

    private final ContractMapper contractMapper;

    private final PushNotificationService pNotification;

    private final CompanyService companyService;

    public ContractService(PushNotificationService pNotification,CompanyService companyService,ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.pNotification = pNotification;
        this.companyService = companyService;
    }

    /**
     * Save a contract.
     *
     * @param contractDTO the entity to save
     * @return the persisted entity
     */
    public ContractDTO save(ContractDTO contractDTO) throws URISyntaxException {
        log.debug("Request to save Contract : {}", contractDTO);
        Contract contract = contractMapper.toEntity(contractDTO);
        contract.setDeleted(contractDTO.getDeleted());
        contract = contractRepository.save(contract);
        CompanyDTO company = this.companyService.findOne(contractDTO.getCompanyId());
        if (contractDTO.getDeleted() == 0 && contractDTO.getId() == null) {
            this.pNotification.sendNotificationAllAdminsByCompanyId(contractDTO.getCompanyId(), this.pNotification.createPushNotification(
                contractDTO.getName() + " - " + company.getName(),
                "Se ha publicado un nuevo contrato."
            ));
        }
        return contractMapper.toDto(contract);
    }

    /**
     * Get all the contracts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ContractDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAll(pageable)
            .map(contractMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<ContractDTO> findAllByCompany(Pageable pageable, Long companyId) {
        log.debug("Request to get all Contracts");
        return contractRepository.findAllByCompanyIdAndDeleted(pageable,companyId,0)
            .map(contractMapper::toDto);
    }

    /**
     * Get one contract by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public ContractDTO findOne(Long id) {
        log.debug("Request to get Contract : {}", id);
        Contract contract = contractRepository.findOne(id);
        return contractMapper.toDto(contract);
    }

    /**
     * Delete the contract by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Contract : {}", id);
        contractRepository.delete(id);
    }
}
