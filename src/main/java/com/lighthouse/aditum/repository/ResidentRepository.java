package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Resident;

import com.lighthouse.aditum.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Resident entity.
 */
@SuppressWarnings("unused")
public interface ResidentRepository extends JpaRepository<Resident,Long> {
    Resident findOneByUserId(Long id);
    List<Resident> findByEnabledAndCompanyIdAndDeleted(Integer state, Long companyId,Integer deleted);
    List<Resident> findByCompanyIdAndDeleted(Long companyId,Integer deleted);
    Resident findByCompanyIdAndIdentificationnumberAndDeleted(Long companyId,String identificationNumber,Integer deleted);
    List<Resident> findByEnabledAndHouseIdAndDeleted(Integer state,Long houseId, Integer deleted);
    Integer countByEnabledAndCompanyIdAndDeleted(Integer state,Long companyId,Integer deleted);
}
