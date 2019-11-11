package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ProcedureVisits;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProcedureVisits entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureVisitsRepository extends JpaRepository<ProcedureVisits, Long> {

}
