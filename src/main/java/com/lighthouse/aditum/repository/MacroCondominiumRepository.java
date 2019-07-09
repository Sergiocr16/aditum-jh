package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.MacroCondominium;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the MacroCondominium entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MacroCondominiumRepository extends JpaRepository<MacroCondominium, Long> {
    @Query("select distinct macro_condominium from MacroCondominium macro_condominium left join fetch macro_condominium.companies")
    List<MacroCondominium> findAllWithEagerRelationships();

    @Query("select macro_condominium from MacroCondominium macro_condominium left join fetch macro_condominium.companies where macro_condominium.id =:id")
    MacroCondominium findOneWithEagerRelationships(@Param("id") Long id);

}
