package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ProcedureComments;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProcedureComments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureCommentsRepository extends JpaRepository<ProcedureComments, Long> {

}
