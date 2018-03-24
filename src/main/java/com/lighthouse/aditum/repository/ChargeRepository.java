package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Charge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Charge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChargeRepository extends JpaRepository<Charge,Long> {
    
}
