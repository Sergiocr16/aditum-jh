package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.dto.MensualAndAnualEgressReportDTO;
import com.lighthouse.aditum.service.dto.MensualAndAnualReportDTO;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.Optional;
import com.lighthouse.aditum.service.MensualAndAnualReportService;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.service.dto.MensualAndAnualIngressReportDTO;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class MensualAndAnualReportResource {
    private final Logger log = LoggerFactory.getLogger(AdminInfoResource.class);

    private static final String ENTITY_NAME = "mensualAndAnualReport";

    private final MensualAndAnualReportService mensualAndAnualReportService;

    public MensualAndAnualReportResource(MensualAndAnualReportService mensualAndAnualReportService) {
        this.mensualAndAnualReportService = mensualAndAnualReportService;
    }

    @Timed
    @Secured({AuthoritiesConstants.MANAGER})
    @GetMapping("/mensualAndAnualReport/{initial_time}/{final_time}/{companyId}")
    public ResponseEntity<MensualAndAnualReportDTO> getMensualAndAnualReport(
        @PathVariable (value = "initial_time")  String initial_time,
        @PathVariable(value = "final_time")  String  final_time,
        @PathVariable(value = "companyId")  Long companyId) {
        log.debug("REST request to info of the dashboard : {}", companyId);
        MensualAndAnualReportDTO mensualAndAnualReportDTO = new MensualAndAnualReportDTO();
        MensualAndAnualIngressReportDTO mensualAndAnualIngressReportDTO = mensualAndAnualReportService.getMensualAndAnualIngressReportDTO(initial_time,final_time,companyId);
        mensualAndAnualReportDTO.setMensualIngressReport(mensualAndAnualIngressReportDTO);

        MensualAndAnualEgressReportDTO mensualAndAnualEgressReportDTO = mensualAndAnualReportService.getMensualAndAnualEgressReportDTO(initial_time,final_time,companyId,mensualAndAnualIngressReportDTO);
        mensualAndAnualReportDTO.setMensualEgressReport(mensualAndAnualEgressReportDTO);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mensualAndAnualReportDTO));
    }
}
