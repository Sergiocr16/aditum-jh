package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.SecurityCompany;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SecurityCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecurityCompanyRepository extends JpaRepository<SecurityCompany,Long> {
    
}
