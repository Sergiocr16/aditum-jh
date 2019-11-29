package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Balance;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Balance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BalanceRepository extends JpaRepository<Balance,Long> {
    Balance findOneByHouseId(Long id);
}
