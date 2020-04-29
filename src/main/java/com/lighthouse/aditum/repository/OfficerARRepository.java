package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.OfficerAR;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the OfficerAR entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OfficerARRepository extends JpaRepository<OfficerAR, Long> {
    @Query("select distinct officerar from OfficerAR officerar left join fetch officerar.houses")
    List<OfficerAR> findAllWithEagerRelationships();

    @Query("select officerar from OfficerAR officerar left join fetch officerar.houses where officerar.id =:id")
    OfficerAR findOneWithEagerRelationships(@Param("id") Long id);

}
