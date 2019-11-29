package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.service.AnualReportDocumentService;
import com.lighthouse.aditum.service.AnualReportService;
import com.lighthouse.aditum.service.MensualReportDocumentService;
import com.lighthouse.aditum.service.dto.*;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import com.lighthouse.aditum.service.MensualReportService;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
public class MensualAndAnualReportResource {
    private final Logger log = LoggerFactory.getLogger(MensualAndAnualReportResource.class);

    private static final String ENTITY_NAME = "mensualAndAnualReport";

    private final MensualReportService mensualReportService;
    private final AnualReportService anualReportService;
    private final MensualReportDocumentService mensualReportDocumentService;
    private final AnualReportDocumentService anualReportDocumentService;
    public MensualAndAnualReportResource(MensualReportService mensualReportService, AnualReportService anualReportService, MensualReportDocumentService mensualReportDocumentService, AnualReportDocumentService anualReportDocumentService) {
        this.mensualReportService = mensualReportService;
        this.anualReportService = anualReportService;
        this.mensualReportDocumentService = mensualReportDocumentService;
        this.anualReportDocumentService = anualReportDocumentService;
    }

    @Timed

    @GetMapping("/mensualReport/{first_month_day}/{final_balance_time}/{initial_time}/{final_time}/{companyId}/{withPresupuesto}")
    public ResponseEntity<MensualReportDTO> getMensualReport(
        @PathVariable("first_month_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime first_month_day,
        @PathVariable("final_balance_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_balance_time,
        @PathVariable("initial_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime initial_time,
        @PathVariable("final_time") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime final_time,
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
        double flujo = mensualReportDTO.getMensualIngressReport().getAllIngressCategoriesTotal() - mensualReportDTO.getMensualEgressReport().getAllEgressCategoriesTotal();
        mensualReportDTO.setFlujo(flujo);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(mensualReportDTO));
    }
    @Timed

    @GetMapping("/anualReport/{actual_month}/{companyId}/{withPresupuesto}")
    public ResponseEntity<AnualReportDTO> getAnualReport(
        @PathVariable (value = "actual_month")  String actual_month,
        @PathVariable(value = "companyId")  Long companyId,
        @PathVariable(value = "withPresupuesto")  int withPresupuesto){
        AnualReportDTO anualReportDTO = new AnualReportDTO();
        anualReportService.getReportByMonth(anualReportDTO,actual_month,companyId,withPresupuesto);



        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(anualReportDTO));
    }

    @GetMapping("/mensualReport/file/{mensualReportObject}")
    @Timed
    public void getFile(@PathVariable String mensualReportObject,
                       HttpServletResponse response) throws URISyntaxException, IOException {
        String[] parts = mensualReportObject.split("}");
        MensualReportDTO mensualReportDTO = new MensualReportDTO();
        MensualIngressReportDTO mensualAndAnualIngressReportDTO = mensualReportService.getMensualAndAnualIngressReportDTO(ZonedDateTime.parse(parts[2]),ZonedDateTime.parse(parts[3]),Long.parseLong(parts[4]),Integer.parseInt(parts[5]));
        mensualReportDTO.setMensualIngressReport(mensualAndAnualIngressReportDTO);
        MensualEgressReportDTO mensualEgressReportDTO = mensualReportService.getMensualAndAnualEgressReportDTO(ZonedDateTime.parse(parts[2]),ZonedDateTime.parse(parts[3]),Long.parseLong(parts[4]),mensualAndAnualIngressReportDTO,Integer.parseInt(parts[5]));
        mensualReportDTO.setMensualEgressReport(mensualEgressReportDTO);
        mensualReportDTO.setMensualAndAnualAccount(mensualReportService.getAccountBalance(ZonedDateTime.parse(parts[0]),ZonedDateTime.parse(parts[1]),Long.parseLong(parts[4])));
        mensualReportDTO.setTotalInitialBalance(mensualReportDTO.getMensualAndAnualAccount());
        double flujo = mensualReportDTO.getMensualIngressReport().getAllIngressCategoriesTotal() - mensualReportDTO.getMensualEgressReport().getAllEgressCategoriesTotal();
        mensualReportDTO.setFlujo(flujo);
        ZonedDateTime utcDateZoned = ZonedDateTime.now(ZoneId.of("Etc/UTC"));
        Locale local = new Locale("es", "ES");
        DateTimeFormatter pattern = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).withLocale(local);

        ZonedDateTime zd_initialTime = ZonedDateTime.parse(parts[2]);
        String initialTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_initialTime);
        ZonedDateTime zd_finalTime = ZonedDateTime.parse(parts[3]);
        String finalTimeFormatted = pattern.ofPattern("dd MMMM yyyy").format(zd_finalTime);



        File file = mensualReportDocumentService.obtainFileToPrint(Long.parseLong(parts[4]),initialTimeFormatted,finalTimeFormatted,mensualReportDTO,Integer.parseInt(parts[5]));
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
    @GetMapping("/anualReport/file/{anualReportObject}")
    @Timed
    public void getAnualReportFile(@PathVariable String anualReportObject,
                        HttpServletResponse response) throws URISyntaxException, IOException {
        String[] parts = anualReportObject.split("}");

        AnualReportDTO anualReportDTO = new AnualReportDTO();
        anualReportService.getReportByMonth(anualReportDTO,parts[0],Long.parseLong(parts[1]),Integer.parseInt(parts[2]));
        File file = anualReportDocumentService.obtainFileToPrint(parts[0],anualReportDTO,Long.parseLong(parts[1]),Integer.parseInt(parts[2]));
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
