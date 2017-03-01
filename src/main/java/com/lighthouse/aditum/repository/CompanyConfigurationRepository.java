package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CompanyConfiguration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CompanyConfiguration entity.
 */
@SuppressWarnings("unused")
public interface CompanyConfigurationRepository extends JpaRepository<CompanyConfiguration,Long> {

}
