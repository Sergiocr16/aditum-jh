package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.WaterConsumption;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the WaterConsumption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WaterConsumptionRepository extends JpaRepository<WaterConsumption, Long> {

    WaterConsumption findByHouseIdAndRecordDate(Long houseId, ZonedDateTime dateTime);

    WaterConsumption findFirstByChargeId(Long chargeId);

    List<WaterConsumption> findByHouseId(Long chargeId);


}
