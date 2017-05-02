package com.lighthouse.aditum.web.websocket;

import com.lighthouse.aditum.service.ResidentService;
import com.lighthouse.aditum.service.dto.HouseDTO;
import com.lighthouse.aditum.service.dto.ResidentDTO;
import com.lighthouse.aditum.service.dto.VehiculeDTO;
import com.lighthouse.aditum.service.dto.VisitantDTO;
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


    public RealTimeCompanyInfoService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @SubscribeMapping("/topic/saveResident/{idCompany}")
    @SendTo("/topic/resident/{idCompany}")
    public ResidentDTO addResident(ResidentDTO residentDTO){
        return residentDTO;
    }

    @SubscribeMapping("/topic/saveVehicle/{idCompany}")
    @SendTo("/topic/vehicle/{idCompany}")
    public VehiculeDTO addVehicule(VehiculeDTO vehicleDTO){
        return vehicleDTO;
    }

    @SubscribeMapping("/topic/saveVisitor/{idCompany}")
    @SendTo("/topic/visitor/{idCompany}")
    public VisitantDTO addVisitor(VisitantDTO visitorDTO){
        return visitorDTO;
    }

    @SubscribeMapping("/topic/saveHouse/{idCompany}")
    @SendTo("/topic/house/{idCompany}")
    public HouseDTO addHouse(HouseDTO houseDTO){
        return houseDTO;
    }

    @SubscribeMapping("/topic/deleteEntity/{idCompany}")
    @SendTo("/topic/deletedEntity/{idCompany}")
    public EntityToDeleteDTO deleteEntity(EntityToDeleteDTO entity){
        return entity;
    }
}
