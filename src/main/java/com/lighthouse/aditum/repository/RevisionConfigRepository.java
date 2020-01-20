package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.RevisionConfig;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RevisionConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionConfigRepository extends JpaRepository<RevisionConfig, Long> {

}
