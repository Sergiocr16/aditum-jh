package com.lighthouse.aditum.service.dto;

import java.io.Serializable;
import java.util.List;

public class AuthorizedUserAccessDoorDTO implements Serializable {

    private String fullName;

    private String description;

    private String condominiumName;

    private String proveedor;

    private String destiny;

    private String houseNumber;

    private String authorizedType;

    private Integer type;

    private Integer vehiculeType;

    private List<CompanyDTO> companiesInvited;

    private String imageUrl;

    private String licenseplate;

    private String vehiculeColor;

    private String vehiculeBrand;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondominiumName() {
        return condominiumName;
    }

    public void setCondominiumName(String condominiumName) {
        this.condominiumName = condominiumName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAuthorizedType() {
        return authorizedType;
    }

    public void setAuthorizedType(String authorizedType) {
        this.authorizedType = authorizedType;
    }

    public List<CompanyDTO> getCompaniesInvited() {
        return companiesInvited;
    }

    public void setCompaniesInvited(List<CompanyDTO> housesInvited) {
        this.companiesInvited = housesInvited;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getVehiculeType() {
        return vehiculeType;
    }

    public void setVehiculeType(Integer vehiculeType) {
        this.vehiculeType = vehiculeType;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }

    public String getVehiculeColor() {
        return vehiculeColor;
    }

    public void setVehiculeColor(String vehiculeColor) {
        this.vehiculeColor = vehiculeColor;
    }

    public String getVehiculeBrand() {
        return vehiculeBrand;
    }

    public void setVehiculeBrand(String vehiculeBrand) {
        this.vehiculeBrand = vehiculeBrand;
    }
}
