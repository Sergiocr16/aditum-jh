package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.HistoricalDefaulter;
import com.lighthouse.aditum.domain.HistoricalPositive;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the HistoricalPositive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoricalPositiveRepository extends JpaRepository<HistoricalPositive, Long> {
    @Query("select h from HistoricalPositive h " +
        "where h.date = ?2 and h.company.id = ?1")
    List<HistoricalPositive> findAllByCompanyIdAndDate(Long companyId, ZonedDateTime date);

    @Query("select h from HistoricalPositive h " +
        "where h.date >=?2 and h.date <=?3 and h.house.id = ?1")
    HistoricalPositive findAllByHouseIdAndDate(Long id, ZonedDateTime firstDate, ZonedDateTime lastDate);
}
