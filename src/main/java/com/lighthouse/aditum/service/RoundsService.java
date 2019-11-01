package com.lighthouse.aditum.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.lighthouse.aditum.service.dto.CheckPointDTO;
import com.lighthouse.aditum.service.dto.RoundConfigurationDTO;
import com.lighthouse.aditum.service.dto.RoundDTO;
import com.lighthouse.aditum.service.dto.RoundScheduleDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class RoundsService {
    private final Logger log = LoggerFactory.getLogger(RoundConfigurationService.class);

    private FireBaseService fireBaseService;

    private final String collectioName = "round";

    public RoundsService(FireBaseService fireBaseService) {
        this.fireBaseService = fireBaseService;
    }



    public List<RoundDTO> getAllByCompanyAndDates(Long companyId, Date initialDate, Date finalDate) throws ExecutionException, InterruptedException {
        List<QueryDocumentSnapshot> documents = this.fireBaseService.getCollectionByCompanAndDatesy(collectioName, companyId.toString(),initialDate,finalDate);
        List<RoundDTO> roundDTOS = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            RoundDTO r = document.toObject(RoundDTO.class);
            r.setUid(document.getId());
//            r.setCheckpoints(this.mapCheckPoints(document));
            roundDTOS.add(r);
        }
        return roundDTOS;
    }
    public RoundDTO getOne(String uid) throws ExecutionException, InterruptedException {
        DocumentSnapshot document = this.fireBaseService.getOneByCollection(collectioName, uid);
            RoundDTO r = document.toObject(RoundDTO.class);
//            r.setCheckpoints(this.mapCheckPoints(document));
        return r;
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
                    boolean done = Boolean.parseBoolean(hashC.get("done").toString());
                    CheckPointDTO c = new CheckPointDTO(latitude, longitude, order);
                    c.setDone(done);
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
