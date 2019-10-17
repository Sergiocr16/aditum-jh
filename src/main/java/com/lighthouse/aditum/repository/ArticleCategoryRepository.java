package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ArticleCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ArticleCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleCategoryRepository extends JpaRepository<ArticleCategory, Long> {
    Page<ArticleCategory> findByDeleted(Pageable pageable, int deleted);
}
