package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Article entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("select distinct article from Article article left join fetch article.references left join fetch article.keyWords left join fetch article.articleCategories")
    List<Article> findAllWithEagerRelationships();

    @Query("select article from Article article left join fetch article.references left join fetch article.keyWords left join fetch article.articleCategories where article.id =:id")
    Article findOneWithEagerRelationships(@Param("id") Long id);


    Page<Article> findByChapterIdAndDeleted(Pageable pageable, Long chapterId, int deleted);

    List<Article> findByChapterIdAndDeleted(Long chapterId, int deleted);
}
