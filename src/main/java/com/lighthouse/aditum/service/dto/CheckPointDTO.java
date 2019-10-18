package com.lighthouse.aditum.service.dto;

public class CheckPointDTO {

    private double latitude;
    private double longitude;
    private Long order;

    public CheckPointDTO() {}

    public CheckPointDTO(double latitude, double longitude, Long order) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.order = order;
    }

    public double isLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double isLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Long isOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }
}
