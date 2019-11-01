package com.lighthouse.aditum.service.dto;


import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class RoundDTO implements Serializable {

    private Date executionDate;
    private boolean finished;
    private boolean inProgress;
    private List<CheckPointDTO> checkpoints;
    private double latitudeCenter;
    private double longitudeCenter;
    private double mapZoom;
    private Long companyId;
    private Date startingTime;
    private Date finishingTime;

    public Date getFinishingTime() {
        return finishingTime;
    }

    public void setFinishingTime(Date finishingTime) {
        this.finishingTime = finishingTime;
    }

    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public RoundDTO() {
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public boolean isInProgress() {
        return inProgress;
    }

    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
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

    public void setLongitudeCenter(double longitudeCenter) {
        this.longitudeCenter = longitudeCenter;
    }

    public double getMapZoom() {
        return mapZoom;
    }

    public void setMapZoom(double mapZoom) {
        this.mapZoom = mapZoom;
    }

    public Date getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public RoundDTO(Date executionDate, boolean finished, boolean inProgress, List<CheckPointDTO> checkpoints, double latitudeCenter, double longitudeCenter, double mapZoom, Date startingTime) {
        this.executionDate = executionDate;
        this.finished = finished;
        this.inProgress = inProgress;
        this.checkpoints = checkpoints;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.mapZoom = mapZoom;
        this.startingTime = startingTime;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<CheckPointDTO> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<CheckPointDTO> checkPoints) {
        this.checkpoints = checkPoints;
    }

}
