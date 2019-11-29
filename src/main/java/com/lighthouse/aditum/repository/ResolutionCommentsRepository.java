package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ResolutionComments;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ResolutionComments entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResolutionCommentsRepository extends JpaRepository<ResolutionComments, Long> {

}
