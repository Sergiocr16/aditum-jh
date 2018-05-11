package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.BalanceByAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;


/**
 * Spring Data JPA repository for the BalanceByAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BalanceByAccountRepository extends JpaRepository<BalanceByAccount,Long> {
    @Query("select e from BalanceByAccount e " +
        "where e.date >= ?1 and e.date <= ?2 and e.accountId = ?3")
    Page<BalanceByAccount> findByDatesBetweenAndAccount(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate, Long accountId);

}
