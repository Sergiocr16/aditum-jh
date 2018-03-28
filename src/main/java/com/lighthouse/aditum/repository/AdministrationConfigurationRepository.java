package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AdministrationConfiguration;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AdministrationConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministrationConfigurationRepository extends JpaRepository<AdministrationConfiguration,Long> {
    
}
