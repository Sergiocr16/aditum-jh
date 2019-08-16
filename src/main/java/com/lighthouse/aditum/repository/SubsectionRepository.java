package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Subsection;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Subsection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsectionRepository extends JpaRepository<Subsection, Long> {

}
