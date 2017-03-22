package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Watch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Watch entity.
 */
@SuppressWarnings("unused")
public interface WatchRepository extends JpaRepository<Watch,Long> {
    Page<Watch> findByCompanyId(Pageable pageable, Long companyId);
    List<Watch> findTop1ByCompanyIdOrderByIdDesc(Long companyId);

    @Query("select w from Watch w " +
        "where w.initialtime >= ?1 and w.finaltime <= ?2 and w.company.id = ?3")
    List<Watch> findByDatesBetween(ZonedDateTime initialDate, ZonedDateTime finalDate,Long companyId);
}
