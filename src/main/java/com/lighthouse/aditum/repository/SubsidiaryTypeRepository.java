package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.SubsidiaryType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the SubsidiaryType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubsidiaryTypeRepository extends JpaRepository<SubsidiaryType, Long> {


    List<SubsidiaryType> findByCompanyIdAndSubsidiaryType(Long id,Integer subsiType);

    List<SubsidiaryType> findByCompanyIdAndSubsidiaryTypeNot(Long id,Integer subsiType);


}
