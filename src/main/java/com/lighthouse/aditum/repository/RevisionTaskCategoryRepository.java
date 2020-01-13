package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.RevisionTaskCategory;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the RevisionTaskCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionTaskCategoryRepository extends JpaRepository<RevisionTaskCategory, Long> {

}
