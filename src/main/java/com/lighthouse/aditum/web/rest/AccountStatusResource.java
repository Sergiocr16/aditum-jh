package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AccountStatusDocumentService;
import com.lighthouse.aditum.service.AccountStatusService;
import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.dto.AccountStatusDTO;

import com.lighthouse.aditum.service.dto.HouseDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
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
    public AccountStatusResource(AccountStatusService accountStatusService, AccountStatusDocumentService accountStatusDocumentService,HouseService houseService) {
        this.accountStatusService = accountStatusService;
        this.accountStatusDocumentService = accountStatusDocumentService;
        this.houseService = houseService;
    }

    @Timed
    @GetMapping("/accountStatus/{houseId}/{initial_time}/{final_time}/{resident_account}/{today_time}")

    public ResponseEntity<AccountStatusDTO> getAccountStatusByHouse(
        @ApiParam Pageable pageable,
        @PathVariable Long houseId,
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time,
        @PathVariable(value = "resident_account")  boolean  resident_account,
        @PathVariable(value = "today_time")  String  today_time
    ) {
        log.debug("REST request to get a page of Charges");
        AccountStatusDTO accountStatusDTO = accountStatusService.getAccountStatusDTO(pageable,houseId,initial_time,final_time,resident_account,today_time);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(accountStatusDTO));
    }

    @GetMapping("/accountStatus/file/{accountStatusObject}")
    @Timed
    public void getAnualReportFile(@PathVariable String accountStatusObject,
                                   HttpServletResponse response) throws URISyntaxException, IOException {

        String[] parts = accountStatusObject.split("}");
        Locale local = new Locale("es", "ES");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);
        ZonedDateTime utcDateZoned = ZonedDateTime.now(ZoneId.of("Etc/UTC"));


        AccountStatusDTO accountStatusDTO = accountStatusService.getAccountStatusDTO(null,Long.parseLong(parts[0]),parts[1],parts[2],Boolean.parseBoolean(parts[3]),parts[4]);

        for (int j = 0; j < accountStatusDTO.getListaAccountStatusItems().size(); j++) {
          accountStatusDTO.getListaAccountStatusItems().get(j).setDateFormatted(pattern.ofPattern("dd MMM yyyy").format(accountStatusDTO.getListaAccountStatusItems().get(j).getDate()));
        }
        HouseDTO houseDTO = houseService.findOne(Long.parseLong(parts[0]));

        ZonedDateTime zd_initialTime = ZonedDateTime.parse(parts[1]+"[America/Regina]");
        String initialTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_initialTime);
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(parts[2]+"[America/Regina]");
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);

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

    }

}
