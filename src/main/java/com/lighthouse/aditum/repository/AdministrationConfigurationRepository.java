package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AdministrationConfiguration;
import com.lighthouse.aditum.domain.Company;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the AdministrationConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministrationConfigurationRepository extends JpaRepository<AdministrationConfiguration,Long> {
    List<AdministrationConfiguration> findByCompanyId(Long companyId);
}
