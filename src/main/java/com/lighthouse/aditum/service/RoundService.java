package com.lighthouse.aditum.service;

import com.lighthouse.aditum.service.dto.CheckPointDTO;
import com.lighthouse.aditum.service.dto.RoundConfigurationDTO;
import com.lighthouse.aditum.service.dto.RoundDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class RoundService {

    private final Logger log = LoggerFactory.getLogger(RoundService.class);
    private final String collectioName = "round";
    private FireBaseService fireBaseService;

    public RoundService(FireBaseService fireBaseService) {
        this.fireBaseService = fireBaseService;
    }


    private void createRound(RoundDTO r, Long companyId) throws ExecutionException, InterruptedException {
        Map<String, Object> data = new HashMap<>();
        data.put("executionDate", r.getExecutionDate());
        data.put("companyId", companyId);
        data.put("checkpoints", this.checkpointsTOHash(r.getCheckpoints()));
        data.put("finished", r.isFinished());
        data.put("inProgress", r.isInProgress());
        data.put("mapZoom", r.getMapZoom());
        data.put("latitudeCenter", r.getLatitudeCenter());
        data.put("longitudeCenter", r.getLongitudeCenter());
        this.fireBaseService.addDocument(collectioName, data);
    }


    public void createRounds(List<RoundConfigurationDTO> rConfigs, Long companyId) {
        rConfigs.forEach(roundConfigurationDTO -> {
            List<String> days = roundConfigurationDTO.getRoundScheduleDTO().getDays();
            for (int i = 0; i < days.size(); i++) {
                if (isToday(days.get(i))) {
                    List<String> hours = roundConfigurationDTO.getRoundScheduleDTO().getHours();
                    for (int j = 0; j < hours.size(); j++) {
                        Date executionDate = this.formatExecutionDate(hours.get(j));
                        RoundDTO r = new RoundDTO(executionDate, false,false, roundConfigurationDTO.getCheckpoints(),roundConfigurationDTO.getLatitudeCenter(),roundConfigurationDTO.getLongitudeCenter(),roundConfigurationDTO.getMapZoom(),null);
                        try {
                            this.createRound(r, companyId);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        });
    }

     private Date formatExecutionDate(String hourS){
         int hour = Integer.parseInt(hourS.split(":")[0]);
         int minute = Integer.parseInt(hourS.split(":")[1].split(" ")[0]);
         String amPm = hourS.split(":")[1].split(" ")[1];
         if (amPm.equals("PM")) {
             if (hour != 12) {
                 hour = hour + 12;
             } else {
                 hour = hour + 11;
             }
         } else {
             if (hour == 12) {
                 hour = 0;
             }
         }
         ZonedDateTime executionDate = ZonedDateTime.now().withHour(hour).withMinute(minute).withSecond(0).withNano(0);
         return Date.from(executionDate.toInstant());
     }
    private ArrayList<Object> checkpointsTOHash(List<CheckPointDTO> cs) {
        ArrayList<Object> arrayExample = new ArrayList<>();
        cs.forEach(checkPointDTO -> {
            Map<String, Object> chash = new HashMap<>();
            chash.put("latitude", checkPointDTO.getLatitude());
            chash.put("longitude", checkPointDTO.getLongitude());
            chash.put("order", checkPointDTO.getOrder());
            chash.put("arrivalTime", checkPointDTO.getArrivalTime());
            chash.put("done", checkPointDTO.isDone());
            arrayExample.add(chash);
        });
        return arrayExample;
    }

    private boolean isToday(String day) {
        int now = ZonedDateTime.now().getDayOfWeek().getValue();
        switch (day) {
            case "d":
                if (now == 0) {
                    return true;
                }
                break;
            case "l":
                if (now == 1) {
                    return true;
                }
                break;
            case "k":
                if (now == 2) {
                    return true;
                }
                break;
            case "m":
                if (now == 3) {
                    return true;
                }
                break;
            case "j":
                if (now == 4) {
                    return true;
                }
                break;
            case "v":
                if (now == 5) {
                    return true;
                }
                break;
            case "s":
                if (now == 6) {
                    return true;
                }
                break;
        }
        return false;
    }

}
