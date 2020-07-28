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

    Page<Complaint> findByCompanyIdAndDeletedAndComplaintCategory(Pageable pageable, Long companyId , int deleted, int category );
    Page<Complaint> findByResidentIdAndDeletedAndComplaintCategory(Pageable pageable, Long residentId , int deleted , int category);
    Page<Complaint> findByResidentIdAndDeletedAndStatusAndComplaintCategory(Pageable pageable, Long residentId , int deleted, int status, int category );
    Page<Complaint> findByCompanyIdAndDeletedAndStatusAndComplaintCategory(Pageable pageable, Long companyId , int deleted, int status, int category );
}
