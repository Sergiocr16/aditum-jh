package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.SubsidiaryCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the SubsidiaryCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsidiaryCategoryRepository extends JpaRepository<SubsidiaryCategory, Long> {

}
