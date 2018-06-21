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
    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.type=?3 and c.state = ?4")
    List<Charge> findPaidChargesBetweenDatesAndCompanyId(ZonedDateTime initialDate, ZonedDateTime finalDate,int type,int state);
    List<Charge> findByHouseIdAndDeletedAndState(Long id,Integer deleted,Integer state);
    List<Charge> findByPaymentIdAndDeletedAndState(Long id,Integer deleted,Integer state);

}
