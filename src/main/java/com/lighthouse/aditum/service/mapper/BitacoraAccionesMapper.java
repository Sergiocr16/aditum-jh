package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BitacoraAcciones and its DTO BitacoraAccionesDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface BitacoraAccionesMapper extends EntityMapper<BitacoraAccionesDTO, BitacoraAcciones> {

    @Mapping(source = "company.id", target = "companyId")
    BitacoraAccionesDTO toDto(BitacoraAcciones bitacoraAcciones); 

    @Mapping(source = "companyId", target = "company")
    BitacoraAcciones toEntity(BitacoraAccionesDTO bitacoraAccionesDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default BitacoraAcciones fromId(Long id) {
        if (id == null) {
            return null;
        }
        BitacoraAcciones bitacoraAcciones = new BitacoraAcciones();
        bitacoraAcciones.setId(id);
        return bitacoraAcciones;
    }
}
