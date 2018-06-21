package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.PresupuestoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Presupuesto and its DTO PresupuestoDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface PresupuestoMapper extends EntityMapper <PresupuestoDTO, Presupuesto> {

    @Mapping(source = "company.id", target = "companyId")
    PresupuestoDTO toDto(Presupuesto presupuesto);

    @Mapping(source = "companyId", target = "company")
    Presupuesto toEntity(PresupuestoDTO presupuestoDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Presupuesto fromId(Long id) {
        if (id == null) {
            return null;
        }
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setId(id);
        return presupuesto;
    }
}
