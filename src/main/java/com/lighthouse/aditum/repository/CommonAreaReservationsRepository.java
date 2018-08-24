package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CommonAreaReservations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommonAreaReservationsRepository extends JpaRepository<CommonAreaReservations,Long> {
    
}
