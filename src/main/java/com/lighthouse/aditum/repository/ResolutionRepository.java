package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Resolution;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Resolution entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, Long> {
    @Query("select distinct resolution from Resolution resolution left join fetch resolution.articles left join fetch resolution.keyWords left join fetch resolution.articleCategories")
    List<Resolution> findAllWithEagerRelationships();

    @Query("select resolution from Resolution resolution left join fetch resolution.articles left join fetch resolution.keyWords left join fetch resolution.articleCategories where resolution.id =:id")
    Resolution findOneWithEagerRelationships(@Param("id") Long id);

}
