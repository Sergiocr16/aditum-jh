package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CustomChargeType;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CustomChargeType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomChargeTypeRepository extends JpaRepository<CustomChargeType, Long> {

    List<CustomChargeType> findAllByCompanyId(Long companyId);

}
