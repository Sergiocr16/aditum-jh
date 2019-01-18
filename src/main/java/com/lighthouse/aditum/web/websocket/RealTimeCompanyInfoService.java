package com.lighthouse.aditum.web.websocket;

import com.lighthouse.aditum.service.HouseService;
import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.service.dto.*;
import com.lighthouse.aditum.web.websocket.dto.EntityToDeleteDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * Created by Sergio on 26/04/2017.
 */
@Controller
public class RealTimeCompanyInfoService {

    private static final Logger log = LoggerFactory.getLogger(ActivityService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    private final HouseService houseService;

    public RealTimeCompanyInfoService(SimpMessageSendingOperations messagingTemplate, HouseService houseService) {
        this.messagingTemplate = messagingTemplate;
        this.houseService = houseService;
    }

    @SubscribeMapping("/topic/sendResident/{idCompany}")
    @SendTo("/topic/resident/{idCompany}")
    public ResidentDTO addResident(ResidentDTO residentDTO) {
        residentDTO.setHouse(houseService.findOne(residentDTO.getHouseId()));
        return residentDTO;
    }

    @SubscribeMapping("/topic/sendVehicle/{idCompany}")
    @SendTo("/topic/vehicle/{idCompany}")
    public VehiculeDTO addVehicule(VehiculeDTO vehicleDTO) {
        vehicleDTO.setHouse(houseService.findOne(vehicleDTO.getHouseId()));
        return vehicleDTO;
    }

    @SubscribeMapping("/topic/sendVisitor/{idCompany}")
    @SendTo("/topic/visitor/{idCompany}")
    public VisitantDTO addVisitor(VisitantDTO visitorDTO) {
        visitorDTO.setHouseNumber(houseService.findOne(visitorDTO.getHouseId()).getHousenumber());
        return visitorDTO;
    }

    @SubscribeMapping("/topic/sendHouse/{idCompany}")
    @SendTo("/topic/house/{idCompany}")
    public HouseDTO addHouse(HouseDTO houseDTO) {
        return houseDTO;
    }

    @SubscribeMapping("/topic/sendOfficer/{idCompany}")
    @SendTo("/topic/officer/{idCompany}")
    public OfficerDTO addOfficer(OfficerDTO officerDTO) {
        return officerDTO;
    }

    @SubscribeMapping("/topic/deleteEntity/{idCompany}")
    @SendTo("/topic/deletedEntity/{idCompany}")
    public EntityToDeleteDTO deleteEntity(EntityToDeleteDTO entity) {
        return entity;
    }
}
