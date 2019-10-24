package com.lighthouse.aditum.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;

public class RoundDTO implements Serializable {

    private ZonedDateTime executionDate;
    private boolean finished;
    private boolean inProgress;
    private List<CheckPointDTO> checkPoints;
    private double latitudeCenter;
    private double longitudeCenter;
    private double mapZoom;

    private ZonedDateTime startingTime;
    public RoundDTO() {
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

    public ZonedDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(ZonedDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public RoundDTO(ZonedDateTime executionDate, boolean finished, boolean inProgress, List<CheckPointDTO> checkPoints, double latitudeCenter, double longitudeCenter, double mapZoom, ZonedDateTime startingTime) {
        this.executionDate = executionDate;
        this.finished = finished;
        this.inProgress = inProgress;
        this.checkPoints = checkPoints;
        this.latitudeCenter = latitudeCenter;
        this.longitudeCenter = longitudeCenter;
        this.mapZoom = mapZoom;
        this.startingTime = startingTime;
    }

    public ZonedDateTime getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public List<CheckPointDTO> getCheckPoints() {
        return checkPoints;
    }

    public void setCheckPoints(List<CheckPointDTO> checkPoints) {
        this.checkPoints = checkPoints;
    }
}
