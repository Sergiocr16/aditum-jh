package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CommonArea;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CommonArea entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommonAreaRepository extends JpaRepository<CommonArea,Long> {
    
}
