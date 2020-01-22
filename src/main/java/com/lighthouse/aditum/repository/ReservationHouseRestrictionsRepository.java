package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ReservationHouseRestrictions;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ReservationHouseRestrictions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReservationHouseRestrictionsRepository extends JpaRepository<ReservationHouseRestrictions, Long> {

    ReservationHouseRestrictions findByHouseIdAndCommonAreaId(Long houseId, Long commonAreaId);

}
