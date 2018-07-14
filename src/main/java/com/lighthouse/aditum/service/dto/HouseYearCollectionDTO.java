package com.lighthouse.aditum.service.dto;

import java.util.List;

/**
 * Created by Sergio on 13/07/2018.
 */
public class HouseYearCollectionDTO {

    private String houseNumber;

    private List<MensualCollectionDTO> yearCollection;


    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public List<MensualCollectionDTO> getYearCollection() {
        return yearCollection;
    }

    public void setYearCollection(List<MensualCollectionDTO> yearCollection) {
        this.yearCollection = yearCollection;
    }
}
