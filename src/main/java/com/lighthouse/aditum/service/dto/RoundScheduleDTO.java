package com.lighthouse.aditum.service.dto;

import java.util.List;

public class RoundScheduleDTO {

    private List<String> days;

    private List<String> hours;

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public List<String> getHours() {
        return hours;
    }

    public void setHours(List<String> hours) {
        this.hours = hours;
    }

    public RoundScheduleDTO(List<String> days, List<String> hours) {
        this.days = days;
        this.hours = hours;
    }

    public RoundScheduleDTO() {
    }

}
