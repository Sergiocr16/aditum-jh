package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Collection;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Collection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CollectionRepository extends JpaRepository<Collection,Long> {
    
}
