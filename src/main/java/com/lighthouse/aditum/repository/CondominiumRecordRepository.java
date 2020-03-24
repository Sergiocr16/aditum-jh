package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CondominiumRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the CondominiumRecord entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CondominiumRecordRepository extends JpaRepository<CondominiumRecord, Long> {

    Page<CondominiumRecord> findAllByCompanyIdAndDeletedAndStatus(Pageable page,Long companyId, int deleted,int state);

}
