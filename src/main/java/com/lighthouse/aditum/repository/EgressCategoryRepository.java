package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.EgressCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the EgressCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgressCategoryRepository extends JpaRepository<EgressCategory,Long> {
    
}
