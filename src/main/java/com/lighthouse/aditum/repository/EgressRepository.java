package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Egress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    Page<Egress> findByDatesBetweenAndCompany(Pageable pageable,ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);
    @Query("select e from Egress e " +
        "where e.expirationDate >= ?1 and e.expirationDate <= ?2 and e.company.id = ?3")
    List<Egress> findByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);
    Page<Egress> findByCompanyId(Pageable pageable, Long companyId);
    @Query("select e from Egress e " +
        "where e.paymentDate >= ?1 and e.paymentDate <= ?2 and e.company.id = ?3 and e.account = ?4")
    Page<Egress> findByDatesBetweenAndCompanyAndAccount(Pageable pageable,ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId,String accountId);
    @Query("select e from Egress e " +
        "where e.date >= ?1 and e.date <= ?2 and e.company.id = ?3")
    Page<Egress> findByCobroDatesBetweenAndCompany(Pageable pageable,ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);
}
