package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Payment and its DTO PaymentDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, })
public interface PaymentMapper extends EntityMapper <PaymentDTO, Payment> {

    @Mapping(source = "house.id", target = "houseId")
    PaymentDTO toDto(Payment payment);

    @Mapping(source = "houseId", target = "house")
    Payment toEntity(PaymentDTO paymentDTO);
    default Payment fromId(Long id) {
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
}
