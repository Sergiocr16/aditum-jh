package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Revision;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Revision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionRepository extends JpaRepository<Revision, Long> {

    Page<Revision> findAllByCompanyId(Pageable page, Long companyId);
}
