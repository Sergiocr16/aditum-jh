package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ChargeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Charge and its DTO ChargeDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, PaymentMapper.class, CompanyMapper.class, })
public interface ChargeMapper extends EntityMapper <ChargeDTO, Charge> {

    @Mapping(source = "house.id", target = "houseId")

    @Mapping(source = "payment.id", target = "paymentId")

    @Mapping(source = "company.id", target = "companyId")
    ChargeDTO toDto(Charge charge);

    @Mapping(source = "houseId", target = "house")

    @Mapping(source = "paymentId", target = "payment")

    @Mapping(source = "companyId", target = "company")

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default Payment paymentFromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }

    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    Charge toEntity(ChargeDTO chargeDTO);
    default Charge fromId(Long id) {
        if (id == null) {
            return null;
        }
        Charge charge = new Charge();
        charge.setId(id);
        return charge;
    }
}
