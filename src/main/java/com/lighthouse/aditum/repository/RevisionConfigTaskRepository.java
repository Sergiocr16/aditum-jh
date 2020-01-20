package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.RevisionConfigTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RevisionConfigTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionConfigTaskRepository extends JpaRepository<RevisionConfigTask, Long> {

}
