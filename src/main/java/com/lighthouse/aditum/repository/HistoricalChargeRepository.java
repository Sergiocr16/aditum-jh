package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.HistoricalCharge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HistoricalCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoricalChargeRepository extends JpaRepository<HistoricalCharge,Long> {
    
}
