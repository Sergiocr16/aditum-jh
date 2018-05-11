package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.repository.TransferenciaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


/**
 * Service Implementation for managing Transferencia.
 */
@Service
@Transactional
public class TransferenciaService {

    private final Logger log = LoggerFactory.getLogger(TransferenciaService.class);

    private final TransferenciaRepository transferenciaRepository;

    public TransferenciaService(TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
    }

    /**
     * Save a transferencia.
     *
     * @param transferencia the entity to save
     * @return the persisted entity
     */
    public Transferencia save(Transferencia transferencia) {
        log.debug("Request to save Transferencia : {}", transferencia);
        return transferenciaRepository.save(transferencia);
    }
    @Transactional(readOnly = true)
    public Page<Transferencia> getBetweenDatesByInComingTransfer(Pageable pageable,String initialTime,String finalTime,int accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return transferenciaRepository.findBetweenDatesByInComingTransfer(pageable,zd_initialTime,zd_finalTime,accountId);
    }
    @Transactional(readOnly = true)
    public Page<Transferencia> getBetweenDatesByOutgoingTransfer(Pageable pageable,String initialTime,String finalTime,int accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return transferenciaRepository.findBetweenDatesByOutgoingTransfer(pageable,zd_initialTime,zd_finalTime,accountId);
    }
    /**
     *  Get all the transferencias.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Transferencia> findAll(Pageable pageable) {
        log.debug("Request to get all Transferencias");
        return transferenciaRepository.findAll(pageable);
    }

    /**
     *  Get one transferencia by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Transferencia findOne(Long id) {
        log.debug("Request to get Transferencia : {}", id);
        return transferenciaRepository.findOne(id);
    }

    /**
     *  Delete the  transferencia by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Transferencia : {}", id);
        transferenciaRepository.delete(id);
    }
}