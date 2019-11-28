package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.SubsidiaryType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SubsidiaryType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsidiaryTypeRepository extends JpaRepository<SubsidiaryType, Long> {

}
