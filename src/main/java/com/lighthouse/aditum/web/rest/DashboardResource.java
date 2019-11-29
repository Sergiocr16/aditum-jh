package com.lighthouse.aditum.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.lighthouse.aditum.security.AuthoritiesConstants;
import com.lighthouse.aditum.service.*;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
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

    private final CompanyConfigurationService companyConfigurationService;

    private final AnualReportService anualReportService;

    private final CollectionService collectionService;

    private final ComplaintService complaintService;

    private final CommonAreaReservationsService commonAreaReservationsService;

    public DashboardResource(AdminInfoService adminInfoService,
                             VehiculeService vehicleService,
                             ResidentService residentServices,
                             VisitantService visitorService,
                             WatchService watchService,
                             HouseService houseService,
                             OfficerService officerService,
                             CompanyConfigurationService companyConfigurationService,
                             AnualReportService anualReportService,
                             CollectionService collectionService,
                             ComplaintService complaintService,
                             CommonAreaReservationsService commonAreaReservationsService
    ) {
        this.adminInfoService = adminInfoService;
        this.vehicleService = vehicleService;
        this.residentServices = residentServices;
        this.visitorService = visitorService;
        this.watchService = watchService;
        this.houseService = houseService;
        this.officerService = officerService;
        this.companyConfigurationService = companyConfigurationService;
        this.anualReportService = anualReportService;
        this.collectionService = collectionService;
        this.complaintService = complaintService;
        this.commonAreaReservationsService = commonAreaReservationsService;
    }


    public DashboardDTO getDashboardDTO(long companyId) throws JSONException {
        Integer enableVehicles = vehicleService.enableQuantityByCompany(companyId);
        Integer disableVehicles = vehicleService.disableQuantityByCompany(companyId);
        Integer vehicleQuantity = enableVehicles + disableVehicles;
        Integer enableResidents = residentServices.enableQuantityByCompany(companyId);
        Integer disableResidents = residentServices.disableQuantityByCompany(companyId);
        Integer residentQuantity = enableResidents + disableResidents;
        Integer visitorInLastMonth = visitorService.countByCompanyInLastMonth(companyId);
        Integer visitorInLastDay = visitorService.countByCompanyInLastDay(companyId);
        Integer visitorAuthorized = visitorService.countByAuthorizedVisitors(companyId);
        ArrayList visitorsPerMonth = visitorService.countVisitantsPerWeek(companyId);
        WatchDTO watchDTO = watchService.findLastWatch(companyId);
        Integer houseQuantity = houseService.countByCompany(companyId);
        Integer desocupatedHousesQuantity = houseService.countByCompanyAndDesocupated(companyId);
        Integer officerQuantity = officerService.countByCompanyId(companyId);
        Integer totalHouses = companyConfigurationService.getByTotalHousesByCompanyId(companyId);

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
        dashboardDTO.setVisitorsPerMonth(visitorsPerMonth);
        dashboardDTO.setTotalHouses(totalHouses);
        dashboardDTO.setComplaintsPending(complaintService.findAllByStatus(null, companyId, 1).getContent().size());
        dashboardDTO.setComplaintsActive(complaintService.findAllByStatus(null, companyId, 2).getContent().size());
        dashboardDTO.setReservationsPending(commonAreaReservationsService.getPendingReservations(null, companyId).getContent().size());
        dashboardDTO.setPendingDevolutions(commonAreaReservationsService.getAcceptedReservations(null,companyId).size());
        return dashboardDTO;
    }

    @Timed
    @Secured({AuthoritiesConstants.JD, AuthoritiesConstants.MANAGER, AuthoritiesConstants.MANAGERMACRO})
    @GetMapping("/dashboard/{companyId}")
    public ResponseEntity<DashboardDTO> getAdminInfo(@PathVariable Long companyId) throws JSONException {
        log.debug("REST request to info of the dashboard : {}", companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(getDashboardDTO(companyId)));
    }

    @Timed
    @Secured({AuthoritiesConstants.JD, AuthoritiesConstants.MANAGER, AuthoritiesConstants.MANAGERMACRO})
    @GetMapping("/dashboard/anualReportIEB/{companyId}/{year}")
    public ResponseEntity<AnualReportDashboardDTO> getAdminInfoIngressEgressBudget(@PathVariable String companyId, @PathVariable String year) throws JSONException {
        log.debug("REST request to info of the ingress, egress and budget for grapsh dashboard : {}", companyId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(anualReportService.getReportByMonthDashboard(Long.parseLong(companyId), Integer.parseInt(year))));
    }

    @Timed
    @Secured({AuthoritiesConstants.JD, AuthoritiesConstants.MANAGER, AuthoritiesConstants.MANAGERMACRO})
    @GetMapping("/dashboard/updateByYear/{companyId}")
    public ArrayList<DashboardVisitorDTO> updateYear(@PathVariable Long companyId) {
        log.debug("REST request to info of the dashboard : {}", companyId);
        return visitorService.countVisitantsPerYear(companyId);
    }

    @Timed
    @Secured({AuthoritiesConstants.JD, AuthoritiesConstants.MANAGER, AuthoritiesConstants.MANAGERMACRO})
    @GetMapping("/dashboard/updateByMonth/{companyId}")
    public ArrayList updateMonth(@PathVariable Long companyId) {
        log.debug("REST request to info of the dashboard : {}", companyId);
        return visitorService.countVisitantsPerMonth(companyId);
    }

    @Timed
    @Secured({AuthoritiesConstants.JD, AuthoritiesConstants.MANAGER, AuthoritiesConstants.MANAGERMACRO})
    @GetMapping("/dashboard/updateByWeek/{companyId}")
    public ArrayList<DashboardVisitorDTO> updateWeek(@PathVariable Long companyId) {
        log.debug("REST request to info of the dashboard : {}", companyId);
        return visitorService.countVisitantsPerWeek(companyId);
    }

    @Timed
    @Secured({AuthoritiesConstants.JD, AuthoritiesConstants.MANAGER, AuthoritiesConstants.MANAGERMACRO})
    @GetMapping("/dashboard/defaulters/{companyId}/{year}")
    public ArrayList<DefaultersMonthDTO> defaulterPerYear(@PathVariable Long companyId, @PathVariable String year) {
        log.debug("REST request to info of the dashboard : {}", companyId);
        return this.collectionService.getDefaulters(companyId, year);
    }
}
