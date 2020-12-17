package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.PaymentChargeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PaymentCharge and its DTO PaymentChargeDTO.
 */
@Mapper(componentModel = "spring", uses = {PaymentMapper.class})
public interface PaymentChargeMapper extends EntityMapper<PaymentChargeDTO, PaymentCharge> {

    @Mapping(source = "payment.id", target = "paymentId")
    PaymentChargeDTO toDto(PaymentCharge paymentCharge); 

    @Mapping(source = "paymentId", target = "payment")
    PaymentCharge toEntity(PaymentChargeDTO paymentChargeDTO);

    default PaymentCharge fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentCharge paymentCharge = new PaymentCharge();
        paymentCharge.setId(id);
        return paymentCharge;
    }
}
