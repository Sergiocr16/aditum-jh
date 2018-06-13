package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


import java.util.List;

import java.time.ZonedDateTime;



/**
 * Spring Data JPA repository for the Charge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChargeRepository extends JpaRepository<Charge,Long> {

    List<Charge> findByHouseIdAndDeletedAndState(Long id,Integer deleted,Integer state);
}
