package com.lighthouse.aditum.service.dto;

import com.lighthouse.aditum.domain.Watch;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

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

    private Integer totalHouses;

    private Integer authorizedVisitantsQuantity;

    private Integer houseQuantity;

    private Integer houseDesocupatedQuantity;

    private Integer officerQuantity;

    private WatchDTO currentWatch;

    private Integer complaintsActive;

    private Integer complaintsPending;

    private Integer pendingDevolutions;

    private Integer reservationsPending;

    private ArrayList<DashboardVisitorDTO>  visitorsPerMonth;

    private AnualReportDashboardDTO anualReportDashboardDTO;

    public AnualReportDashboardDTO getAnualReportDashboardDTO() {
        return anualReportDashboardDTO;
    }

    public void setAnualReportDashboardDTO(AnualReportDashboardDTO anualReportDashboardDTO) {
        this.anualReportDashboardDTO = anualReportDashboardDTO;
    }

    public Integer getTotalHouses() {
        return totalHouses;
    }

    public void setTotalHouses(Integer totalHouses) {
        this.totalHouses = totalHouses;
    }

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

    public ArrayList<DashboardVisitorDTO> getVisitorsPerMonth() {
        return visitorsPerMonth;
    }

    public void setVisitorsPerMonth(ArrayList<DashboardVisitorDTO>  visitorsPerMonth) {
        this.visitorsPerMonth = visitorsPerMonth;
    }


    public Integer getComplaintsActive() {
        return complaintsActive;
    }

    public void setComplaintsActive(Integer complaintsActive) {
        this.complaintsActive = complaintsActive;
    }

    public Integer getComplaintsPending() {
        return complaintsPending;
    }

    public void setComplaintsPending(Integer complaintsPending) {
        this.complaintsPending = complaintsPending;
    }

    public Integer getReservationsPending() {
        return reservationsPending;
    }

    public void setReservationsPending(Integer reservationsPending) {
        this.reservationsPending = reservationsPending;
    }

    public Integer getPendingDevolutions() {
        return pendingDevolutions;
    }

    public void setPendingDevolutions(Integer pendingDevolutions) {
        this.pendingDevolutions = pendingDevolutions;
    }
}
