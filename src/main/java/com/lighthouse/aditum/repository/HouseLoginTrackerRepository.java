package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.House;
import com.lighthouse.aditum.domain.HouseLoginTracker;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the HouseLoginTracker entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HouseLoginTrackerRepository extends JpaRepository<HouseLoginTracker, Long> {

    HouseLoginTracker findOneByHouseId(Long houseId);

}
