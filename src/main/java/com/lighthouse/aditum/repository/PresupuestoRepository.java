package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Presupuesto;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Presupuesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto,Long> {
    List<Presupuesto> findByCompanyId(Long companyId);
}
