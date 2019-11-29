package com.lighthouse.aditum.service.dto;

public class DashboardVisitorDTO {
    private String name;
    private int dataValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return dataValue;
    }

    public void setValue(int dataValue) {
        this.dataValue = dataValue;
    }
}
