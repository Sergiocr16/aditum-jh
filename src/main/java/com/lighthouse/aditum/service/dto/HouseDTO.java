package com.lighthouse.aditum.service.dto;


import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the House entity.
 */
public class HouseDTO implements Serializable {

    private Long id;

    @NotNull
    private String housenumber;

    private String extension;

    @NotNull
    @Min(value = 0)
    @Max(value = 1)
    private Integer isdesocupated;

    private ZonedDateTime desocupationinitialtime;

    private ZonedDateTime desocupationfinaltime;

    private String securityKey;

    private String emergencyKey;

    private String loginCode;

    private Integer codeStatus;

    private String due;

    private String squareMeters;

    private Long companyId;

    private BalanceDTO balance;

    private List<ChargeDTO> chargesToPay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Integer getIsdesocupated() {
        return isdesocupated;
    }

    public void setIsdesocupated(Integer isdesocupated) {
        this.isdesocupated = isdesocupated;
    }

    public ZonedDateTime getDesocupationinitialtime() {
        return desocupationinitialtime;
    }

    public void setDesocupationinitialtime(ZonedDateTime desocupationinitialtime) {
        this.desocupationinitialtime = desocupationinitialtime;
    }

    public ZonedDateTime getDesocupationfinaltime() {
        return desocupationfinaltime;
    }

    public void setDesocupationfinaltime(ZonedDateTime desocupationfinaltime) {
        this.desocupationfinaltime = desocupationfinaltime;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public String getEmergencyKey() {
        return emergencyKey;
    }

    public void setEmergencyKey(String emergencyKey) {
        this.emergencyKey = emergencyKey;
    }

    public String getLoginCode() {
        return loginCode;
    }

    public void setLoginCode(String loginCode) {
        this.loginCode = loginCode;
    }

    public Integer getCodeStatus() {
        return codeStatus;
    }

    public void setCodeStatus(Integer codeStatus) {
        this.codeStatus = codeStatus;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(String squareMeters) {
        this.squareMeters = squareMeters;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        HouseDTO houseDTO = (HouseDTO) o;
        if(houseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), houseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "HouseDTO{" +
            "id=" + getId() +
            ", housenumber='" + getHousenumber() + "'" +
            ", extension='" + getExtension() + "'" +
            ", isdesocupated='" + getIsdesocupated() + "'" +
            ", desocupationinitialtime='" + getDesocupationinitialtime() + "'" +
            ", desocupationfinaltime='" + getDesocupationfinaltime() + "'" +
            ", securityKey='" + getSecurityKey() + "'" +
            ", emergencyKey='" + getEmergencyKey() + "'" +
            ", loginCode='" + getLoginCode() + "'" +
            ", codeStatus='" + getCodeStatus() + "'" +
            ", due='" + getDue() + "'" +
            ", squareMeters='" + getSquareMeters() + "'" +
            "}";
    }

    public BalanceDTO getBalance() {
        return balance;
    }

    public void setBalance(BalanceDTO balance) {
        this.balance = balance;
    }

    public List<ChargeDTO> getChargesToPay() {
        return chargesToPay;
    }

    public void setChargesToPay(List<ChargeDTO> chargesToPay) {
        this.chargesToPay = chargesToPay;
    }
}
