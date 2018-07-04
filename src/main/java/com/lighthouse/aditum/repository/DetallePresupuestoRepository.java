package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.DetallePresupuesto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the DetallePresupuesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetallePresupuestoRepository extends JpaRepository<DetallePresupuesto,Long> {
    Page<DetallePresupuesto> findByPresupuestoId(Pageable pageable, String budgetId);
    List<DetallePresupuesto> findByPresupuestoIdAndType(String budgetId, String type);
}
