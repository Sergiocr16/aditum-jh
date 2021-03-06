package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Subsidiary;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Subsidiary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

    List<Subsidiary> findByHouseId(Long houseId);

}
