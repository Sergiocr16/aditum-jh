package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.dto.AccountStatusDTO;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    public AccountStatusResource(AccountStatusService accountStatusService) {
        this.accountStatusService = accountStatusService;
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

}
