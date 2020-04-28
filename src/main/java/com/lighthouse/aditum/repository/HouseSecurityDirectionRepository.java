package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.HouseSecurityDirection;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.ArrayList;


/**
 * Spring Data JPA repository for the HouseSecurityDirection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HouseSecurityDirectionRepository extends JpaRepository<HouseSecurityDirection, Long> {
    ArrayList<HouseSecurityDirection> findByCompanyId(Long companyId);
    HouseSecurityDirection findOneByHouseId(Long houseId);

}
