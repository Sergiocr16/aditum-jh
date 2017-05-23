package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.AdminInfoDTO;
import com.lighthouse.aditum.service.dto.DashboardDTO;
import com.lighthouse.aditum.service.dto.WatchDTO;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

/**
 * Created by Sergio on 23/05/2017.
 */
@RestController
@RequestMapping("/api")
public class DashboardResource {

    private final Logger log = LoggerFactory.getLogger(AdminInfoResource.class);

    private static final String ENTITY_NAME = "dashboard";

    private final AdminInfoService adminInfoService;

    private final VehiculeService vehicleService;

    private final ResidentService residentServices;

    private final VisitantService visitorService;

    private final WatchService watchService;

    private final HouseService houseService;

    private final OfficerService officerService;

    public DashboardResource(AdminInfoService adminInfoService,
                             VehiculeService vehicleService,
                             ResidentService residentServices,
                             VisitantService visitorService,
                             WatchService watchService,
                             HouseService houseService,
                             OfficerService officerService

    ) {
        this.adminInfoService = adminInfoService;
        this.vehicleService = vehicleService;
        this.residentServices = residentServices;
        this.visitorService = visitorService;
        this.watchService = watchService;
        this.houseService = houseService;
        this.officerService = officerService;
    }
    @Timed
    @Secured({AuthoritiesConstants.MANAGER})
    @GetMapping("/dashboard/{companyId}")
    public ResponseEntity<DashboardDTO> getAdminInfo(@PathVariable Long companyId) {
        log.debug("REST request to info of the dashboard : {}", companyId);
        Integer enableVehicles = vehicleService.enableQuantityByCompany(companyId);
        Integer disableVehicles = vehicleService.disableQuantityByCompany(companyId);
        Integer vehicleQuantity = enableVehicles + disableVehicles;
        Integer enableResidents = residentServices.enableQuantityByCompany(companyId);
        Integer disableResidents = residentServices.disableQuantityByCompany(companyId);
        Integer residentQuantity = enableResidents + disableResidents;
        Integer visitorInLastMonth = visitorService.countByCompanyInLastMonth(companyId);
        Integer visitorInLastDay = visitorService.countByCompanyInLastDay(companyId);
        Integer visitorAuthorized = visitorService.countByAuthorizedVisitors(companyId);
        WatchDTO watchDTO = watchService.findLastWatch(companyId);
        Integer houseQuantity = houseService.countByCompany(companyId);
        Integer desocupatedHousesQuantity = houseService.countByCompanyAndDesocupated(companyId);
        Integer officerQuantity = officerService.countByCompanyId(companyId);
       DashboardDTO dashboardDTO = new DashboardDTO();
       dashboardDTO.setEnableResidentQuantity(enableResidents);
       dashboardDTO.setDisableResidentQuantity(disableResidents);
       dashboardDTO.setResidentQuantity(residentQuantity);
       dashboardDTO.setEnableVehicleuQantity(enableVehicles);
       dashboardDTO.setDisableVehicleQuantity(disableVehicles);
       dashboardDTO.setVehicleQuantity(vehicleQuantity);
       dashboardDTO.setMonthVisitantsQuantity(visitorInLastMonth);
       dashboardDTO.setDayVisitantsQuantity(visitorInLastDay);
       dashboardDTO.setAuthorizedVisitantsQuantity(visitorAuthorized);
       dashboardDTO.setCurrentWatch(watchDTO);
       dashboardDTO.setHouseDesocupatedQuantity(desocupatedHousesQuantity);
       dashboardDTO.setHouseQuantity(houseQuantity);
       dashboardDTO.setOfficerQuantity(officerQuantity);

        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dashboardDTO));
    }

}
