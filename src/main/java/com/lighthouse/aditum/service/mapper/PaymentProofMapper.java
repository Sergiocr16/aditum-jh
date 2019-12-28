package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.PaymentProofDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity PaymentProof and its DTO PaymentProofDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class, PaymentMapper.class})
public interface PaymentProofMapper extends EntityMapper<PaymentProofDTO, PaymentProof> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "payment.id", target = "paymentId")
    PaymentProofDTO toDto(PaymentProof paymentProof);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "paymentId", target = "payment")
    PaymentProof toEntity(PaymentProofDTO paymentProofDTO);

    default PaymentProof fromId(Long id) {
        if (id == null) {
            return null;
        }
        PaymentProof paymentProof = new PaymentProof();
        paymentProof.setId(id);
        return paymentProof;
    }
}
