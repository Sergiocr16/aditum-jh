package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.HistoricalDefaulterCharge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the HistoricalDefaulterCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoricalDefaulterChargeRepository extends JpaRepository<HistoricalDefaulterCharge, Long> {

    List<HistoricalDefaulterCharge> findAllByHistoricalDefaulterId(Long id);

}
