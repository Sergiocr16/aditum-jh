package com.lighthouse.aditum.repository;

import org.springframework.data.domain.Page;

import com.lighthouse.aditum.domain.Contract;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Contract entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    Page<Contract> findAllByCompanyIdAndDeleted(Pageable pageable, Long companyId, Integer deleted);


}
