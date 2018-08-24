package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AnnouncementComment;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;


/**
 * Spring Data  repository for the AnnouncementComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AnnouncementCommentRepository extends JpaRepository<AnnouncementComment, Long> {

    int countAnnouncementCommentByAnnouncement_Id(Long id);

    Page<AnnouncementComment> findAnnouncementCommentByAnnouncement_Id(Pageable pageable, Long id);
}
