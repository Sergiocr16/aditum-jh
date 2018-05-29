package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.DetallePresupuesto;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DetallePresupuesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetallePresupuestoRepository extends JpaRepository<DetallePresupuesto,Long> {
    
}
