package com.lighthouse.aditum.service.dto;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class RoundConfigurationDTO {


    private Long companyId;
    private double latitudeCenter;
    private double longitudeCenter;
    private String name;
    private List<CheckPointDTO> checkpoints;
    private RoundScheduleDTO roundScheduleDTO;
    private long mapZoom;

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

    public RoundConfigurationDTO() {
    }

    public long getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(long mapZoom) {
        this.mapZoom = mapZoom;
    }

    public RoundConfigurationDTO(Long companyId, double latitudeCenter, double longitudeCenter, String name, Long mapZoom, List<CheckPointDTO> checkpoints) throws JSONException {
        this.companyId = companyId;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.name = name;
        this.checkpoints = checkpoints;
        this.mapZoom = mapZoom;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public double getLatitudeCenter() {
        return latitudeCenter;
    }

    public void setLatitudeCenter(double latitudeCenter) {
        this.latitudeCenter = latitudeCenter;
    }

    public double getLongitudeCenter() {
        return longitudeCenter;
    }

    public void setLongitudeCenter(Double longitudeCenter) {
        this.longitudeCenter = longitudeCenter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
