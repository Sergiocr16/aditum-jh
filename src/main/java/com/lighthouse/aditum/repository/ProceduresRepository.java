package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Procedures;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Procedures entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProceduresRepository extends JpaRepository<Procedures, Long> {

}
