package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.DetallePresupuestoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DetallePresupuesto and its DTO DetallePresupuestoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DetallePresupuestoMapper extends EntityMapper <DetallePresupuestoDTO, DetallePresupuesto> {
    
    
    default DetallePresupuesto fromId(Long id) {
        if (id == null) {
            return null;
        }
        DetallePresupuesto detallePresupuesto = new DetallePresupuesto();
        detallePresupuesto.setId(id);
        return detallePresupuesto;
    }
}
