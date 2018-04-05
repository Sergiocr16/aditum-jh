package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Egress;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the Egress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgressRepository extends JpaRepository<Egress,Long> {
    @Query("select e from Egress e " +
    "where e.expirationDate >= ?1 and e.expirationDate <= ?2 and e.company.id = ?3")
    List<Egress> findByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);
}
