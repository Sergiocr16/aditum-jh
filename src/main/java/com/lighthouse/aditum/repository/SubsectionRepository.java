package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Subsection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Subsection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsectionRepository extends JpaRepository<Subsection, Long> {

    Page<Subsection> findByArticleIdAndDeleted(Pageable pageable, Long articleId, int deleted);

}
