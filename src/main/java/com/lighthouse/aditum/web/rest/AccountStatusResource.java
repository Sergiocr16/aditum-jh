package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AccountStatusService;
import com.lighthouse.aditum.service.ChargeService;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import com.lighthouse.aditum.web.rest.util.PaginationUtil;
import com.lighthouse.aditum.service.dto.ChargeDTO;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Charge.
 */
@RestController
@RequestMapping("/api")
public class AccountStatusResource {

    private final Logger log = LoggerFactory.getLogger(ChargeResource.class);

    private static final String ENTITY_NAME = "charge";

    private final AccountStatusService accountStatusService;

    public AccountStatusResource(AccountStatusService accountStatusService) {
        this.accountStatusService = accountStatusService;
    }


    @GetMapping("/accountStatus/{houseId}/{initial_time}/{final_time}/")
    @Timed
    public ResponseEntity<List<ChargeDTO>> getAccountStatusByHouse(
        @PathVariable Long houseId,
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time
    )
        throws URISyntaxException {
        log.debug("REST request to get a page of Charges");
        Page<ChargeDTO> page = accountStatusService.findAllByHouse(houseId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/charges");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
