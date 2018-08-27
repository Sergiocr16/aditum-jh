package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Announcement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    Page<Announcement> findByCompanyIdAndStatusAndDeleted(Pageable pageable, Long companyId, Integer status, Integer deleted);

    @Query("select e from Announcement e " +
        "where e.company.id = ?1 and (e.status= ?2 or e.status =?3) and e.deleted = 0 order by e.publishingDate desc")
    Page<Announcement> findByCompanyIdAndStatusAdmin(Pageable pageable, Long companyId, Integer status1, Integer status2);
}
