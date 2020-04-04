package com.lighthouse.aditum.service.dto;


import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the AccessDoor entity.
 */
public class CreateAllChargesDTO implements Serializable {
 List<CreateChargeDTO> charges;

    public List<CreateChargeDTO> getCharges() {
        return charges;
    }

    public void setCharges(List<CreateChargeDTO> charges) {
        this.charges = charges;
    }
}
