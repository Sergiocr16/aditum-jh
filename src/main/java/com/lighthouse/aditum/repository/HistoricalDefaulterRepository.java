package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.HistoricalDefaulter;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the HistoricalDefaulter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HistoricalDefaulterRepository extends JpaRepository<HistoricalDefaulter, Long> {

    @Query("select h from HistoricalDefaulter h " +
        "where h.date >=?2 and h.date <=?3 and h.company.id = ?1")
    List<HistoricalDefaulter> findAllByCompanyIdAndDate(Long companyId, ZonedDateTime firstDate,ZonedDateTime lastDate);

    @Query("select h from HistoricalDefaulter h " +
        "where h.date >=?2 and h.date <=?3 and h.house.id = ?1")
    List<HistoricalDefaulter> findAllByHouseIdAndDate(Long id, ZonedDateTime firstDate,ZonedDateTime lastDate);

}
