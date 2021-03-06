package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AccountStatusDocumentService;
import com.lighthouse.aditum.service.AccountStatusService;
import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.service.dto.AccountStatusDTO;

import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
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

    public AccountStatusResource(AccountStatusService accountStatusService, AccountStatusDocumentService accountStatusDocumentService,HouseService houseService, ResidentService residentService) {
        this.accountStatusService = accountStatusService;
        this.accountStatusDocumentService = accountStatusDocumentService;
        this.houseService = houseService;
        this.residentService = residentService;
    }

    @Timed
    @GetMapping("/accountStatus/{houseId}/{initial_time}/{final_time}/{resident_account}/{today_time}")

    public ResponseEntity<AccountStatusDTO> getAccountStatusByHouse(
        @ApiParam Pageable pageable,
        @PathVariable Long houseId,
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
        @PathVariable(value = "resident_account")  boolean  resident_account,
        @PathVariable("today_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime today_time
    ) {
        log.debug("REST request to get a page of Charges");
        AccountStatusDTO accountStatusDTO = accountStatusService.getAccountStatusDTO(pageable,houseId,initial_time,final_time,resident_account,today_time);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountStatusDTO));
    }

    @GetMapping("/accountStatus/file/{accountStatusObject}/{option}")
    @Timed
    public void getAnualReportFile(@PathVariable String accountStatusObject, @PathVariable int option,
                                   HttpServletResponse response) throws URISyntaxException, IOException {

        String[] parts = accountStatusObject.split("}");
        Locale local = new Locale("es", "ES");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);
        ZonedDateTime utcDateZoned = ZonedDateTime.now(ZoneId.of("Etc/UTC"));
        AccountStatusDTO accountStatusDTO = accountStatusService.getAccountStatusDTO(null,Long.parseLong(parts[0]),ZonedDateTime.parse(parts[1]),ZonedDateTime.parse(parts[2]),Boolean.parseBoolean(parts[3]),ZonedDateTime.parse(parts[4]));
        for (int j = 0; j < accountStatusDTO.getListaAccountStatusItems().size(); j++) {
          accountStatusDTO.getListaAccountStatusItems().get(j).setDateFormatted(pattern.ofPattern("dd MMM yyyy").format(accountStatusDTO.getListaAccountStatusItems().get(j).getDate()));
        }
        HouseDTO houseDTO = houseService.findOne(Long.parseLong(parts[0]));
        ZonedDateTime zd_initialTime = ZonedDateTime.parse(parts[1]);
        String initialTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_initialTime);
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(parts[2]);
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);
        if(option==1){
            File file = accountStatusDocumentService.obtainFileToPrint(accountStatusDTO,houseDTO,initialTimeFormatted,finalTimeFormatted);
            FileInputStream stream = new FileInputStream(file);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename="+file.getName());
            IOUtils.copy(stream,response.getOutputStream());
            stream.close();
            new Thread() {
                @Override
                public void run() {
                    try {
                        this.sleep(400000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    file.delete();

                }
            }.start();

        }else if(option==2){
            Page<ResidentDTO> residents = residentService.findEnabledByHouseId(null, houseDTO.getId());
            List<ResidentDTO> emailTo = new ArrayList<>();
            for (int i = 0; i < residents.getContent().size(); i++) {
                if (residents.getContent().get(i).getPrincipalContact() == 1) {
                    emailTo.add(residents.getContent().get(i));
                }
            }
            accountStatusDTO.setEmailTo(emailTo);
            accountStatusDocumentService.sendEmail(accountStatusDTO,houseDTO,initialTimeFormatted,finalTimeFormatted);

        }



    }

}
