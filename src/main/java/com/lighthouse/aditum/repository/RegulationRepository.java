package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Regulation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Regulation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegulationRepository extends JpaRepository<Regulation, Long> {
    Page<Regulation> findByDeleted(Pageable pageable, int deleted);

    Page<Regulation> findByDeletedAndCompanyId(Pageable pageable, int deleted, Long companyId);

}
