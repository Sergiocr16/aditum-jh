package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.CommonAreaReservationsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CommonAreaReservations and its DTO CommonAreaReservationsDTO.
 */
@Mapper(componentModel = "spring", uses = {CommonAreaMapper.class, CompanyMapper.class, ChargeMapper.class, })
public interface CommonAreaReservationsMapper extends EntityMapper <CommonAreaReservationsDTO, CommonAreaReservations> {

    @Mapping(source = "commonArea.id", target = "commonAreaId")

    @Mapping(source = "company.id", target = "companyId")

    @Mapping(source = "chargeId.id", target = "chargeIdId")
    CommonAreaReservationsDTO toDto(CommonAreaReservations commonAreaReservations); 

    @Mapping(source = "commonAreaId", target = "commonArea")

    @Mapping(source = "companyId", target = "company")

    @Mapping(source = "chargeIdId", target = "chargeId")
    CommonAreaReservations toEntity(CommonAreaReservationsDTO commonAreaReservationsDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default CommonAreaReservations fromId(Long id) {
        if (id == null) {
            return null;
        }
        CommonAreaReservations commonAreaReservations = new CommonAreaReservations();
        commonAreaReservations.setId(id);
        return commonAreaReservations;
    }
}
