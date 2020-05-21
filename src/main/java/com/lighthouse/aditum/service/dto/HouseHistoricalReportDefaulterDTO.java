
package com.lighthouse.aditum.service.dto;


import com.lighthouse.aditum.service.util.RandomUtil;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the House entity.
 */
public class HouseHistoricalReportDefaulterDTO implements Serializable {

    private Long id;

    @NotNull
    private String housenumber;

    private List<ChargeDTO> charges;

    private double totalDue = 0;

    private String totalDueFormatted;

    public double getTotalDue() {
        return totalDue;
    }

    public void setTotalDue(String currency, double totalDue) {
        this.setTotalDueFormatted(RandomUtil.formatMoney(currency, totalDue));
        this.totalDue = totalDue;
    }

    public String getTotalDueFormatted() {
        return totalDueFormatted;
    }

    public void setTotalDueFormatted(String totalDueFormatted) {
        this.totalDueFormatted = totalDueFormatted;
    }

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

    public List<ChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<ChargeDTO> charges) {
        this.charges = charges;
    }
}
