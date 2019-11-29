package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.OfficerAccount;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the OfficerAccount entity.
 */
@SuppressWarnings("unused")
public interface OfficerAccountRepository extends JpaRepository<OfficerAccount,Long> {
    OfficerAccount findOneByUserId(Long id);
    Page<OfficerAccount> findByCompanyId(Pageable pageable, Long companyId);
}
