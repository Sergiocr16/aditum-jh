package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ComplaintComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ComplaintComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment, Long> {

    Page<ComplaintComment> findComplaintCommentByComplaintIdAndDeleted(Pageable pageable, Long complaintId, int deleted);
}
