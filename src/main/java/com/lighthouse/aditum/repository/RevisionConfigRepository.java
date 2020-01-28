package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.RevisionConfig;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import org.springframework.data.domain.Pageable;


/**
 * Spring Data JPA repository for the RevisionConfig entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionConfigRepository extends JpaRepository<RevisionConfig, Long> {

    Page<RevisionConfig> findAllByCompanyIdAndDeleted(Pageable pageable, Long companyId, int deleted);

}
