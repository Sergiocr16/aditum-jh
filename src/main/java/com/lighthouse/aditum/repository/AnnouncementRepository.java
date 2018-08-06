package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Announcement;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Announcement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    
}
