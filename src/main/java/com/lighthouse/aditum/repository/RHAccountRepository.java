package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.RHAccount;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the RHAccount entity.
 */
@SuppressWarnings("unused")
public interface RHAccountRepository extends JpaRepository<RHAccount,Long> {

    @Query("select distinct rHAccount from RHAccount rHAccount left join fetch rHAccount.companies")
    List<RHAccount> findAllWithEagerRelationships();

    @Query("select rHAccount from RHAccount rHAccount left join fetch rHAccount.companies where rHAccount.id =:id")
    RHAccount findOneWithEagerRelationships(@Param("id") Long id);

}
