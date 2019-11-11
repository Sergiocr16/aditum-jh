package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ProcedureSteps;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProcedureSteps entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureStepsRepository extends JpaRepository<ProcedureSteps, Long> {

}
