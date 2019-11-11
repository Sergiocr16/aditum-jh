package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ProcedureVisitRanking;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ProcedureVisitRanking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProcedureVisitRankingRepository extends JpaRepository<ProcedureVisitRanking, Long> {

}
