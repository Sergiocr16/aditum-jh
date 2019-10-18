package com.lighthouse.aditum.service.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RoundConfigurationDTO {


    private Long companyId;
    private Long latitudeCenter;
    private Long longitudeCenter;
    private String name;
    private List<CheckPointDTO> checkpoints;
    private RoundScheduleDTO roundScheduleDTO;

    public List<CheckPointDTO> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckPointDTO> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public RoundScheduleDTO getRoundScheduleDTO() {
        return roundScheduleDTO;
    }

    public void setRoundScheduleDTO(RoundScheduleDTO roundScheduleDTO) {
        this.roundScheduleDTO = roundScheduleDTO;
    }

    public RoundConfigurationDTO() {}
    public RoundConfigurationDTO(Long companyId, Long latitudeCenter, Long longitudeCenter, String name, List<CheckPointDTO> checkpoints) throws JSONException {
        this.companyId = companyId;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.name = name;
       this.checkpoints = checkpoints;
        String  a = "";
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getLatitudeCenter() {
        return latitudeCenter;
    }

    public void setLatitudeCenter(Long latitudeCenter) {
        this.latitudeCenter = latitudeCenter;
    }

    public Long getLongitudeCenter() {
        return longitudeCenter;
    }

    public void setLongitudeCenter(Long longitudeCenter) {
        this.longitudeCenter = longitudeCenter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
