package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Presupuesto;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the Presupuesto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PresupuestoRepository extends JpaRepository<Presupuesto,Long> {
    List<Presupuesto> findByCompanyIdAndDeleted(Long companyId,int deleted);

    @Query("select e from Presupuesto e " +
        "where e.date >= ?1 and e.date <= ?2 and e.company.id = ?3 and e.deleted = ?4")
    List<Presupuesto> findByBudgetsDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId,int deleted);

}
