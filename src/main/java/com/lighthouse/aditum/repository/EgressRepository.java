package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Egress;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Egress entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgressRepository extends JpaRepository<Egress,Long> {
    
}
