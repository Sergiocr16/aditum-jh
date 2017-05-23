package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.domain.Watch;

import java.io.Serializable;

/**
 * Created by Sergio on 23/05/2017.
 */
public class DashboardDTO implements Serializable {

    private Integer vehicleQuantity;

    private Integer enableVehicleuQantity;

    private Integer disableVehicleQuantity;

    private Integer residentQuantity;

    private Integer enableResidentQuantity;

    private Integer disableResidentQuantity;

    private Integer monthVisitantsQuantity;

    private Integer dayVisitantsQuantity;

    private Integer authorizedVisitantsQuantity;

    private Integer houseQuantity;

    private Integer houseDesocupatedQuantity;

    private Integer officerQuantity;

    private WatchDTO currentWatch;


    public Integer getVehicleQuantity() {
        return vehicleQuantity;
    }

    public void setVehicleQuantity(Integer vehicleQuantity) {
        this.vehicleQuantity = vehicleQuantity;
    }

    public void setCurrentWatch(WatchDTO currentWatch) {
        this.currentWatch = currentWatch;
    }
    public WatchDTO getCurrentWatch() {
        return this.currentWatch;
    }
    public Integer getEnableVehicleuQantity() {
        return enableVehicleuQantity;
    }

    public void setEnableVehicleuQantity(Integer enableVehicleuQantity) {
        this.enableVehicleuQantity = enableVehicleuQantity;
    }

    public Integer getDisableVehicleQuantity() {
        return disableVehicleQuantity;
    }

    public void setDisableVehicleQuantity(Integer disableVehicleQuantity) {
        this.disableVehicleQuantity = disableVehicleQuantity;
    }

    public Integer getResidentQuantity() {
        return residentQuantity;
    }

    public void setResidentQuantity(Integer residentQuantity) {
        this.residentQuantity = residentQuantity;
    }

    public Integer getEnableResidentQuantity() {
        return enableResidentQuantity;
    }

    public void setEnableResidentQuantity(Integer enableResidentQuantity) {
        this.enableResidentQuantity = enableResidentQuantity;
    }

    public Integer getDisableResidentQuantity() {
        return disableResidentQuantity;
    }

    public void setDisableResidentQuantity(Integer disableResidentQuantity) {
        this.disableResidentQuantity = disableResidentQuantity;
    }

    public Integer getMonthVisitantsQuantity() {
        return monthVisitantsQuantity;
    }

    public void setMonthVisitantsQuantity(Integer monthVisitantsQuantity) {
        this.monthVisitantsQuantity = monthVisitantsQuantity;
    }

    public Integer getDayVisitantsQuantity() {
        return dayVisitantsQuantity;
    }

    public void setDayVisitantsQuantity(Integer dayVisitantsQuantity) {
        this.dayVisitantsQuantity = dayVisitantsQuantity;
    }

    public Integer getAuthorizedVisitantsQuantity() {
        return authorizedVisitantsQuantity;
    }

    public void setAuthorizedVisitantsQuantity(Integer authorizedVisitantsQuantity) {
        this.authorizedVisitantsQuantity = authorizedVisitantsQuantity;
    }

    public Integer getHouseQuantity() {
        return houseQuantity;
    }

    public void setHouseQuantity(Integer houseQuantity) {
        this.houseQuantity = houseQuantity;
    }

    public Integer getHouseDesocupatedQuantity() {
        return houseDesocupatedQuantity;
    }

    public void setHouseDesocupatedQuantity(Integer houseDesocupatedQuantity) {
        this.houseDesocupatedQuantity = houseDesocupatedQuantity;
    }

    public Integer getOfficerQuantity() {
        return officerQuantity;
    }

    public void setOfficerQuantity(Integer officerQuantity) {
        this.officerQuantity = officerQuantity;
    }
}
