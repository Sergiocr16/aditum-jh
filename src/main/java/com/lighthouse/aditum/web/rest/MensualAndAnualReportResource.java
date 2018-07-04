package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.dto.MensualEgressReportDTO;
import com.lighthouse.aditum.service.dto.MensualReportDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import com.lighthouse.aditum.service.MensualReportService;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.service.dto.MensualIngressReportDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class MensualAndAnualReportResource {
    private final Logger log = LoggerFactory.getLogger(AdminInfoResource.class);

    private static final String ENTITY_NAME = "mensualAndAnualReport";

    private final MensualReportService mensualReportService;

    public MensualAndAnualReportResource(MensualReportService mensualReportService) {
        this.mensualReportService = mensualReportService;
    }

    @Timed
    @Secured({AuthoritiesConstants.MANAGER})
    @GetMapping("/mensualReport/{first_month_day}/{final_balance_time}/{initial_time}/{final_time}/{companyId}/{withPresupuesto}")
    public ResponseEntity<MensualReportDTO> getMensualReport(
        @PathVariable (value = "first_month_day")  String first_month_day,
        @PathVariable (value = "final_balance_time")  String final_balance_time,
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time,
        @PathVariable(value = "companyId")  Long companyId,
        @PathVariable(value = "withPresupuesto")  int withPresupuesto){
        log.debug("REST request to info of the dashboard : {}", companyId);
        MensualReportDTO mensualReportDTO = new MensualReportDTO();
        MensualIngressReportDTO mensualAndAnualIngressReportDTO = mensualReportService.getMensualAndAnualIngressReportDTO(initial_time,final_time,companyId,withPresupuesto);
        mensualReportDTO.setMensualIngressReport(mensualAndAnualIngressReportDTO);

        MensualEgressReportDTO mensualEgressReportDTO = mensualReportService.getMensualAndAnualEgressReportDTO(initial_time,final_time,companyId,mensualAndAnualIngressReportDTO,withPresupuesto);
        mensualReportDTO.setMensualEgressReport(mensualEgressReportDTO);

        mensualReportDTO.setMensualAndAnualAccount(mensualReportService.getAccountBalance(first_month_day,final_balance_time,companyId));
        mensualReportDTO.setTotalInitialBalance(mensualReportDTO.getMensualAndAnualAccount());
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mensualReportDTO));
    }
    @Timed
    @Secured({AuthoritiesConstants.MANAGER})
    @GetMapping("/anualReport/{actual_month}/{companyId}/{withPresupuesto}")
    public ResponseEntity<MensualReportDTO> getAnualReport(
        @PathVariable (value = "actual_month")  String actual_month,
        @PathVariable(value = "companyId")  Long companyId,
        @PathVariable(value = "withPresupuesto")  int withPresupuesto){
        log.debug("REST request to info of the dashboard : {}", companyId);
        MensualReportDTO mensualReportDTO = new MensualReportDTO();

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mensualReportDTO));
    }
}
