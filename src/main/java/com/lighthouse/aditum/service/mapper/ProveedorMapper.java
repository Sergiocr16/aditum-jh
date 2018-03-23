package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.ProveedorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Proveedor and its DTO ProveedorDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class, })
public interface ProveedorMapper extends EntityMapper <ProveedorDTO, Proveedor> {

    @Mapping(source = "company.id", target = "companyId")
    ProveedorDTO toDto(Proveedor proveedor);

    @Mapping(source = "companyId", target = "company")
    Proveedor toEntity(ProveedorDTO proveedorDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Proveedor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Proveedor proveedor = new Proveedor();
        proveedor.setId(id);
        return proveedor;
    }
}
