package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.EmailConfiguration;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EmailConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailConfigurationRepository extends JpaRepository<EmailConfiguration, Long> {

    EmailConfiguration findByCompanyId(Long companyId);

}
