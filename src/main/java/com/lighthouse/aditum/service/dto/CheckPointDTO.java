package com.lighthouse.aditum.service.dto;

import java.util.Date;

public class CheckPointDTO {

    private double latitude;
    private double longitude;
    private Long order;
    private Date arrivalTime;
    private boolean done;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

    public Date getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public CheckPointDTO() {}

    public CheckPointDTO(double latitude, double longitude, Long order) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.order = order;
    }

}
