package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Complaint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    Page<Complaint> findByCompanyIdAndDeleted(Pageable pageable, Long companyId , int deleted );
    Page<Complaint> findByCompanyIdAndDeletedAndStatus(Pageable pageable, Long companyId , int deleted, int status );
}
