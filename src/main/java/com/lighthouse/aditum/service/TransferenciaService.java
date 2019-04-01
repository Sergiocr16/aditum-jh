package com.lighthouse.aditum.service;

import com.lighthouse.aditum.domain.Transferencia;
import com.lighthouse.aditum.repository.TransferenciaRepository;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;


/**
 * Service Implementation for managing Transferencia.
 */
@Service
@Transactional
public class TransferenciaService {

    private final Logger log = LoggerFactory.getLogger(TransferenciaService.class);

    private final TransferenciaRepository transferenciaRepository;

    private final BitacoraAccionesService bitacoraAccionesService;

    private final AdminInfoService adminInfoService;

    private final UserService userService;
    public TransferenciaService(UserService userService,AdminInfoService adminInfoService,BitacoraAccionesService bitacoraAccionesService,TransferenciaRepository transferenciaRepository) {
        this.transferenciaRepository = transferenciaRepository;
        this.bitacoraAccionesService = bitacoraAccionesService;
        this.adminInfoService = adminInfoService;
        this.userService = userService;
    }

    /**
     * Save a transferencia.
     *
     * @param transferencia the entity to save
     * @return the persisted entity
     */
    public Transferencia save(Transferencia transferencia) {
        log.debug("Request to save Transferencia : {}", transferencia);
        Transferencia transferencia1 = transferenciaRepository.save(transferencia);

        LocalDateTime today = LocalDateTime.now();
        ZoneId id = ZoneId.of("America/Costa_Rica");  //Create timezone
        ZonedDateTime zonedDateTime = ZonedDateTime.of(today, id);
        BitacoraAccionesDTO bitacoraAccionesDTO = new BitacoraAccionesDTO();


        bitacoraAccionesDTO.setType(7);
        bitacoraAccionesDTO.setConcept("Transferencia: " + transferencia.getConcepto() + ", de la cuenta " + transferencia.getCuentaOrigen() + " a la cuenta " + transferencia.getCuentaDestino() + ", por " + formatColonesD(Integer.parseInt( transferencia.getMonto())) + " colones");



        bitacoraAccionesDTO.setEjecutionDate(zonedDateTime);
        bitacoraAccionesDTO.setCategory("Ingresos");

        bitacoraAccionesDTO.setIdReference(transferencia.getId());

        bitacoraAccionesDTO.setCompanyId(Long.parseLong(transferencia.getIdCompany()+""));
        bitacoraAccionesDTO.setIdResponsable(adminInfoService.findOneByUserId(userService.getUserWithAuthorities().getId()).getId());
        bitacoraAccionesService.save(bitacoraAccionesDTO);




        return transferencia1;
    }


    private String formatColonesD(double text) {
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if (text == 0) {
            return currencyFormatter.format(text).substring(1);
        } else {
            String t = currencyFormatter.format(text).substring(1);
            return t.substring(0, t.length() - 3).replace(",", ".");
        }
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
    @Transactional(readOnly = true)
    public List<Transferencia> getBetweenDatesByInComingTransfer(String initialTime,String finalTime,int accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return transferenciaRepository.findBetweenDatesByInComingTransfer(zd_initialTime,zd_finalTime,accountId);
    }
    @Transactional(readOnly = true)
    public List<Transferencia> getBetweenDatesByOutgoingTransfer(String initialTime, String finalTime, int accountId) {
        log.debug("Request to get all Visitants in last month by house");
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(initialTime+"[America/Regina]");
        ZonedDateTime zd_finalTime = ZonedDateTime.parse((finalTime+"[America/Regina]").replace("00:00:00","23:59:59"));
        return transferenciaRepository.findBetweenDatesByOutgoingTransfer(zd_initialTime,zd_finalTime,accountId);
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
