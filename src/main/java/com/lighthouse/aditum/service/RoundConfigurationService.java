package com.lighthouse.aditum.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.lighthouse.aditum.service.dto.CheckPointDTO;
import com.lighthouse.aditum.service.dto.RoundConfigurationDTO;
import com.lighthouse.aditum.service.dto.RoundScheduleDTO;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class RoundConfigurationService {

    private final Logger log = LoggerFactory.getLogger(RoundConfigurationService.class);

    private FireBaseService fireBaseService;

    private final String collectioName = "round-configuration";

    public RoundConfigurationService(FireBaseService fireBaseService) {
        this.fireBaseService = fireBaseService;
    }

    public List<RoundConfigurationDTO> getAllByCompany(String companyId) throws ExecutionException, InterruptedException, JSONException {
        List<QueryDocumentSnapshot> documents = this.fireBaseService.getCollectionByCompany(collectioName, companyId);
        List<RoundConfigurationDTO> roundConfigurationDTOS = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            System.out.println(document.getId());
            RoundConfigurationDTO r = document.toObject(RoundConfigurationDTO.class);
            r.setCheckpoints(this.mapCheckPoints(document));
            r.setRoundScheduleDTO(this.mapSchedule(document));
            roundConfigurationDTOS.add(r);
        }
        return roundConfigurationDTOS;
    }


    private List<CheckPointDTO> mapCheckPoints(DocumentSnapshot document) {
        Map<String, Object> map = document.getData();
        List<CheckPointDTO> checkPointDTOS = new ArrayList<>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("checkpoints")) {
                for (int i = 0; i < ((ArrayList<HashMap>) entry.getValue()).size(); i++) {
                    HashMap hashC = ((ArrayList<HashMap>) entry.getValue()).get(i);
                    double latitude = Double.parseDouble(hashC.get("latitude").toString());
                    double longitude = Double.parseDouble(hashC.get("longitude").toString());
                    long order = Long.parseLong(hashC.get("order").toString());
                    CheckPointDTO c = new CheckPointDTO(latitude, longitude, order);
                    checkPointDTOS.add(c);
                }
            }
        }
        return checkPointDTOS;
    }

    private RoundScheduleDTO mapSchedule(DocumentSnapshot document) {
        Map<String, Object> map = (Map<String, Object>)document.getData().get("schedule");
        List<CheckPointDTO> checkPointDTOS = new ArrayList<>();
        RoundScheduleDTO rs = new RoundScheduleDTO(new ArrayList<String>(),new ArrayList<String>());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("hours")) {
                for (int i = 0; i < ((ArrayList<HashMap>) entry.getValue()).size(); i++) {
                    HashMap h = ((ArrayList<HashMap>) entry.getValue()).get(i);
                     rs.getHours().add(h.get("hour").toString());
                }
            }
            if (entry.getKey().equals("days")) {
                for (int i = 0; i < ((ArrayList<String>) entry.getValue()).size(); i++) {
                    String d = ((ArrayList<String>) entry.getValue()).get(i).toString();
                    rs.getDays().add(d);
                }
            }
        }
        return rs;
    }
}
