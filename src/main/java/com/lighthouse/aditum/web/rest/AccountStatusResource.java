package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.*;

import com.lighthouse.aditum.service.util.RandomUtil;
import com.lowagie.text.DocumentException;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * REST controller for managing Charge.
 */
@RestController
@RequestMapping("/api")
public class AccountStatusResource {

    private final Logger log = LoggerFactory.getLogger(AccountStatusResource.class);

    private static final String ENTITY_NAME = "accountStatus";

    private final AccountStatusService accountStatusService;
    private final AccountStatusDocumentService accountStatusDocumentService;
    private final HouseService houseService;
    private final ResidentService residentService;
    private final ChargeService chargeService;
    private final CompanyConfigurationService companyConfigurationService;
    private final AdministrationConfigurationService administrationConfigurationService;

    public AccountStatusResource(AdministrationConfigurationService administrationConfigurationService, CompanyConfigurationService companyConfigurationService, ChargeService chargeService, AccountStatusService accountStatusService, AccountStatusDocumentService accountStatusDocumentService, HouseService houseService, ResidentService residentService) {
        this.accountStatusService = accountStatusService;
        this.chargeService = chargeService;
        this.accountStatusDocumentService = accountStatusDocumentService;
        this.houseService = houseService;
        this.residentService = residentService;
        this.companyConfigurationService = companyConfigurationService;
        this.administrationConfigurationService = administrationConfigurationService;
    }

    @Timed
    @GetMapping("/accountStatus/{houseId}/{initial_time}/{final_time}/{resident_account}/{today_time}")

    public ResponseEntity<AccountStatusDTO> getAccountStatusByHouse(
        @ApiParam Pageable pageable,
        @PathVariable String houseId,
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "resident_account") boolean resident_account,
        @PathVariable("today_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime today_time
    ) {
        Long houseIdD = Long.parseLong(RandomUtil.decrypt(houseId));
        log.debug("REST request to get a page of Charges");
        AccountStatusDTO accountStatusDTO = accountStatusService.getAccountStatusDTO(null, houseIdD, initial_time, final_time, resident_account, today_time);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountStatusDTO));
    }


    @GetMapping("/accountStatus/send-to/{houseId}/{companyId}/{emailTo}/month/{monthDate}")
    @Timed
    public void sendAccountStatus(@PathVariable Long houseId, @PathVariable Long companyId, @PathVariable String emailTo, @PathVariable ZonedDateTime monthDate) throws URISyntaxException, IOException, DocumentException {
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        AdministrationConfigurationDTO administrationConfiguration = this.administrationConfigurationService.findOneByCompanyId(companyId);
        this.accountStatusService.sendAccountStatusByEmail(houseId, companyId, emailTo, monthDate, currency, administrationConfiguration);
    }

    @GetMapping("/accountStatus/send-to-all/toAll/{toAll}/{companyId}/month/{monthDate}")
    @Timed
    public void sendAccountStatus(@PathVariable Long companyId, @PathVariable Boolean toAll, @PathVariable ZonedDateTime monthDate) throws URISyntaxException, IOException, DocumentException {
        String currency = companyConfigurationService.getByCompanyId(null, companyId).getContent().get(0).getCurrency();
        AdministrationConfigurationDTO administrationConfiguration = this.administrationConfigurationService.findOneByCompanyId(companyId);
        List<HouseDTO> houses = this.houseService.findWithBalance(companyId).getContent();
        for (int i = 0; i < houses.size(); i++) {
            HouseDTO house = houses.get(i);
            if(toAll==true){
                List<ResidentDTO> rs = this.residentService.findOwnerByHouse(house.getId()+"");
                String mailTo = "";
                for (int j = 0; j < rs.size(); j++) {
                    if(rs.get(j).getDeleted()==0) {
                        mailTo = mailTo + rs.get(j).getId() + ",";
                    }
                }
                if (!rs.equals("")) {
                    this.accountStatusService.sendAccountStatusByEmail(house.getId(), companyId, mailTo, monthDate, currency, administrationConfiguration);
                }
            }else{
                if(Double.parseDouble(house.getBalance().getTotal())!=0){
                    List<ResidentDTO> rs = this.residentService.findOwnerByHouse(house.getId()+"");
                    String mailTo = "";
                    for (int j = 0; j < rs.size(); j++) {
                        if(rs.get(j).getDeleted()==0){
                            mailTo = mailTo + rs.get(j).getId()+",";
                        }
                    }
                    if (!rs.equals("")) {
                        this.accountStatusService.sendAccountStatusByEmail(house.getId(), companyId, mailTo, monthDate, currency, administrationConfiguration);
                    }
                }
            }

        }
    }

    @GetMapping("/accountStatus/file/{accountStatusObject}/{option}")
    @Timed
    public void getAnualReportFile(@PathVariable String accountStatusObject, @PathVariable int option,
                                   HttpServletResponse response) throws URISyntaxException, IOException {
        String[] parts = accountStatusObject.split("}");
        Locale locale = new Locale("es", "CR");
        DateTimeFormatter spanish = DateTimeFormatter.ofPattern("dd MMMM yyyy", locale);
        AccountStatusDTO accountStatusDTO = accountStatusService.getAccountStatusDTO(null, Long.parseLong(parts[0]), ZonedDateTime.parse(parts[1]), ZonedDateTime.parse(parts[2]), false, ZonedDateTime.parse(parts[4]));
        for (int j = 0; j < accountStatusDTO.getListaAccountStatusItems().size(); j++) {
            accountStatusDTO.getListaAccountStatusItems().get(j).setDateFormatted(spanish.format(accountStatusDTO.getListaAccountStatusItems().get(j).getDate()));
        }
        HouseDTO houseDTO = houseService.findOne(Long.parseLong(parts[0]));
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(parts[1]);
        String initialTimeFormatted = spanish.format(zd_initialTime);
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(parts[2]);
        String finalTimeFormatted = spanish.format(zd_finalTime);
        if (option == 1) {
            File file = accountStatusDocumentService.obtainFileToPrint(accountStatusDTO, houseDTO, initialTimeFormatted, finalTimeFormatted);
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

        } else if (option == 2) {
            Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null, houseDTO.getId());
            List<ResidentDTO> emailTo = new ArrayList<>();
            for (int i = 0; i < residents.getContent().size(); i++) {
                if (residents.getContent().get(i).getPrincipalContact() == 1) {
                    emailTo.add(residents.getContent().get(i));
                }
            }
            accountStatusDTO.setEmailTo(emailTo);
            accountStatusDocumentService.sendEmail(accountStatusDTO, houseDTO, initialTimeFormatted, finalTimeFormatted);

        }


    }

}
