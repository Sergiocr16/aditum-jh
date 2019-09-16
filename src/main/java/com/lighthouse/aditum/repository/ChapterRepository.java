package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Chapter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Page<Chapter> findByRegulationIdAndDeleted(Pageable pageable, Long regulationId, int deleted);
    List<Chapter> findByRegulationIdAndDeleted(Long regulationId, int deleted);
}
