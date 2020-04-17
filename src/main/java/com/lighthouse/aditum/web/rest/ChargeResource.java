package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static com.lighthouse.aditum.service.util.RandomUtil.formatDateTime;
import static com.lighthouse.aditum.service.util.RandomUtil.formatMoney;

/**
 * REST controller for managing Charge.
 */
@RestController
@RequestMapping("/api")
public class ChargeResource {

    private final Logger log = LoggerFactory.getLogger(ChargeResource.class);

    private static final String ENTITY_NAME = "charge";

    private final ChargeService chargeService;

    private final ChargesToPayDocumentService chargesToPayDocumentService;

    private final PaymentDocumentService paymentEmailSenderService;

    private final AdministrationConfigurationService administrationConfigurationService;

    private final HouseService houseService;

    private final PushNotificationService pNotification;

    private final CompanyConfigurationService companyConfigurationService;

    private final WaterConsumptionService waterConsumptionService;


    public ChargeResource(WaterConsumptionService waterConsumptionService,CompanyConfigurationService companyConfigurationService, PushNotificationService pNotification, HouseService houseService, AdministrationConfigurationService administrationConfigurationService, PaymentDocumentService paymentEmailSenderService, ChargeService chargeService, ChargesToPayDocumentService chargesToPayDocumentService) {
        this.chargeService = chargeService;
        this.chargesToPayDocumentService = chargesToPayDocumentService;
        this.paymentEmailSenderService = paymentEmailSenderService;
        this.administrationConfigurationService = administrationConfigurationService;
        this.houseService = houseService;
        this.pNotification = pNotification;
        this.companyConfigurationService = companyConfigurationService;
        this.waterConsumptionService = waterConsumptionService;
    }

    /**
     * POST  /charges : Create a new charge.
     *
     * @param chargeDTO the chargeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chargeDTO, or with status 400 (Bad Request) if the charge has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/charges")
    @Timed
    public ResponseEntity<ChargeDTO> createCharge(@Valid @RequestBody ChargeDTO chargeDTO) throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to save Charge : {}", chargeDTO);
        if (chargeDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new entity cannot already have an ID")).body(null);
        }
        HouseDTO houseDTO = this.houseService.findOne(chargeDTO.getHouseId());
        AdministrationConfigurationDTO administrationConfigurationDTO = this.administrationConfigurationService.findOneByCompanyId(houseDTO.getCompanyId());
        CompanyConfigurationDTO companyConfigDTO = this.companyConfigurationService.findOne(houseDTO.getCompanyId());
        chargeDTO.setDate(formatDateTime(chargeDTO.getDate()));
        if(chargeDTO.getSubcharge()==null){
            chargeDTO.setSubcharge("0");
        }
        ChargeDTO result = chargeService.save(administrationConfigurationDTO, chargeDTO);
        if (chargeDTO.getDate().isBefore(ZonedDateTime.now()) && chargeDTO.isSendEmail()) {
            this.pNotification.sendNotificationsToOwnersByHouse(chargeDTO.getHouseId(),
                this.pNotification.createPushNotification(chargeDTO.getConcept() + " - " + houseDTO.getHousenumber(),
                    "Se ha creado una nueva cuota en su filial por un monto de " + companyConfigDTO.getCurrency() + "" + formatMoney(companyConfigDTO.getCurrency(), Double.parseDouble(chargeDTO.getAmmount())) + "."));
            this.paymentEmailSenderService.sendChargeEmail(administrationConfigurationDTO, this.houseService.findOne(chargeDTO.getHouseId()), result);
        }
        return ResponseEntity.created(new URI("/api/charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @GetMapping("/charges/create/extraordinary-all")
    @Timed
    public ResponseEntity<CreateAllChargesDTO> createChargeExtra(@RequestBody CreateAllChargesDTO createAllChargesDTO) throws URISyntaxException, IOException, DocumentException {
      int cosecutive = chargeService.obtainConsecutive(createAllChargesDTO.getCharges().get(0).getCompanyId())-1;
      for(CreateChargeDTO c : createAllChargesDTO.getCharges()){
          c.setConsecutive(cosecutive);
          cosecutive += cosecutive+1;
          this.createCharge(mapNewChargeDTOtoChargeDTO(c));
      }
        return ResponseEntity.ok()
            .body(createAllChargesDTO);
    }

    private ChargeDTO mapNewChargeDTOtoChargeDTO(CreateChargeDTO c){
        ChargeDTO newC = new ChargeDTO();
   newC.setConsecutive(c.getConsecutive());
   newC.setDeleted(c.getDeleted());
   newC.setAmmount(c.getAmmount()+"");
   newC.setCompanyId(c.getCompanyId());
   newC.setState(c.getState());
   newC.setType(Integer.parseInt(c.getType()));
   newC.setDate(c.getDate());
  newC.setConcept(c.getConcept());
  newC.setHouseId(c.getHouseId());
  return newC;
    }
    /**
     * PUT  /charges : Updates an existing charge.
     *
     * @param chargeDTO the chargeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chargeDTO,
     * or with status 400 (Bad Request) if the chargeDTO is not valid,
     * or with status 500 (Internal Server Error) if the chargeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/charges")
    @Timed
    public ResponseEntity<ChargeDTO> updateCharge(@Valid @RequestBody ChargeDTO chargeDTO) throws URISyntaxException, IOException, DocumentException {
        log.debug("REST request to update Charge : {}", chargeDTO);
        if (chargeDTO.getId() == null) {
            return createCharge(chargeDTO);
        }
        ChargeDTO result = chargeService.update(chargeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chargeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /charges : get all the charges.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of charges in body
     */
    @GetMapping("/charges")
    @Timed
    public ResponseEntity<List<ChargeDTO>> getAllCharges(@ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Charges");
        Page<ChargeDTO> page = chargeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/charges-file/{chargeId}")
    @Timed
    public void getFile(@PathVariable Long chargeId, HttpServletResponse response) throws URISyntaxException, IOException, DocumentException {
        File file = chargeService.obtainFileToPrint(chargeId);
        FileInputStream stream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename="+file.getName());
        IOUtils.copy(stream,response.getOutputStream());
        stream.close();
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                file.delete();
            }
        }.start();
    }
    @GetMapping("/chargesPerHouse/{houseId}")
    @Timed
    public ResponseEntity<List<ChargeDTO>> getAllChargesByHouse(@PathVariable Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Charges");
        Page<ChargeDTO> page = chargeService.findAllByHouse(houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/waterChargeByHouse/{houseId}")
    @Timed
    public ResponseEntity<List<ChargeDTO>> getWaterChargesByHouse(@PathVariable Long houseId)
        throws URISyntaxException {
        log.debug("REST request to get a page of Charges");
        Page<ChargeDTO> page = chargeService.findWaterChargeAllByHouse(houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/waterCharges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    @GetMapping("/formatCompanyId")
    @Timed
    public void formatCompany()
        throws URISyntaxException {
        List<CompanyConfigurationDTO> companyConfigurationDTO = this.companyConfigurationService.findAll(null).getContent();
        for (CompanyConfigurationDTO companyConfiguration : companyConfigurationDTO) {
            List<HouseDTO> houseDTOS = this.houseService.findAll(companyConfiguration.getCompanyId()).getContent();
            for (HouseDTO houseDTO : houseDTOS) {
                List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouseToFormat(houseDTO.getId()).getContent();
                for (ChargeDTO chargeDTO : chargeDTOS) {
                    chargeDTO.setCompanyId(houseDTO.getCompanyId());
                    this.chargeService.saveFormat(chargeDTO);
                }
            }
        }
        for (CompanyConfigurationDTO companyConfiguration : companyConfigurationDTO) {
            List<HouseDTO> houseDTOS = this.houseService.findAll(companyConfiguration.getCompanyId()).getContent();
            for (HouseDTO houseDTO : houseDTOS) {
                List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouseToFormat(houseDTO.getId()).getContent();
                for (ChargeDTO chargeDTO : chargeDTOS) {
                    if (chargeDTO.getSplited() != null) {
                        this.chargeService.saveFormatSplitted(chargeDTO);
                    }
                }
            }
        }
    }

    @GetMapping("/formatCompanyWaterCharges")
    @Timed
    public void formatCompanyWaterCharges() throws URISyntaxException {
        List<CompanyConfigurationDTO> companyConfigurationDTO = this.companyConfigurationService.findAll(null).getContent();
        for (CompanyConfigurationDTO companyConfiguration : companyConfigurationDTO) {
            List<HouseDTO> houseDTOS = this.houseService.findAll(companyConfiguration.getCompanyId()).getContent();
            for (HouseDTO houseDTO : houseDTOS) {
                List<ChargeDTO> chargeDTOS = this.chargeService.findAllByHouseToFormat(houseDTO.getId()).getContent();
                for (ChargeDTO chargeDTO : chargeDTOS) {
                    if(chargeDTO.getType()==6 && chargeDTO.getDeleted()==0){
                        List<WaterConsumptionDTO> wcs = this.waterConsumptionService.findByHouseId(houseDTO.getId());
                        for(WaterConsumptionDTO wc : wcs){
                            if(chargeDTO.getAmmount().equals(wc.getMonth()) && wc.getStatus()==1){
                                wc.setChargeId(chargeDTO.getId());
                                this.waterConsumptionService.save(wc);
                            }
                        }
                    }
                }
            }
        }
    }

    @GetMapping("/charges/chargesToPay/{final_time}/{type}/byCompany/{companyId}")
    @Timed
    public ResponseEntity<ChargesToPayReportDTO> getAllChargesToPay(
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "type") int type,
        @PathVariable(value = "companyId") Long companyId,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Charges");
        ChargesToPayReportDTO report = chargeService.findChargesToPay(final_time, type, companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }


    @GetMapping("/charges/billingReport/{initial_time}/{final_time}/byCompany/{companyId}/{houseId}/{category}")
    @Timed
    public ResponseEntity<BillingReportDTO> getBillingReport(
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "companyId") Long companyId,
        @PathVariable(value = "houseId") String houseId,
        @PathVariable(value = "category") String category,
        @ApiParam Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Charges");
        BillingReportDTO report = chargeService.findBillingReport(initial_time,final_time, companyId,houseId,category);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(report));
    }

    @GetMapping("/charges/billingReport/file/{initial_time}/{final_time}/{companyId}/{houseId}/{category}")
    @Timed
    public void getBillingReport(@PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
                                 @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
                                 @PathVariable(value = "companyId") Long companyId,
                                 @PathVariable(value = "houseId") String houseId,
                                 @PathVariable(value = "category") String category,
                                 @ApiParam Pageable pageable, HttpServletResponse response)
        throws URISyntaxException, IOException  {
        log.debug("REST request to get a page of Charges");
        BillingReportDTO report = chargeService.findBillingReport(initial_time,final_time, companyId,houseId,category);
        File file = chargeService.obtainBillingReportToPrint(initial_time,final_time, companyId,houseId,category);
        FileInputStream stream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename="+file.getName());
        IOUtils.copy(stream,response.getOutputStream());
        stream.close();
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                file.delete();

            }
        }.start();
    }

    /**
     * GET  /charges/:id : get the "id" charge.
     *
     * @param id the id of the chargeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chargeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/charges/{id}")
    @Timed
    public ResponseEntity<ChargeDTO> getCharge(@PathVariable Long id) {
        log.debug("REST request to get Charge : {}", id);
        ChargeDTO chargeDTO = chargeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chargeDTO));
    }

    /**
     * DELETE  /charges/:id : delete the "id" charge.
     *
     * @param id the id of the chargeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/charges/{id}")
    @Timed
    public ResponseEntity<Void> deleteCharge(@PathVariable Long id) {
        log.debug("REST request to delete Charge : {}", id);
        chargeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/charges/chargesToPay/file/{final_time}/{type}/byCompany/{companyId}")
    @Timed
    public void getFile(
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "type") int type,
        @PathVariable(value = "companyId") Long companyId,
        @ApiParam Pageable pageable,
        HttpServletResponse response) throws URISyntaxException, IOException {
        File file = chargesToPayDocumentService.obtainFileToPrint(final_time, type, companyId);
        FileInputStream stream = new FileInputStream(file);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + file.getName());
        IOUtils.copy(stream, response.getOutputStream());
        stream.close();
        new Thread() {
            @Override
            public void run() {
                try {
                    this.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                file.delete();

            }
        }.start();
    }
}
