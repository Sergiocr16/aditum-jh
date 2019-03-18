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
    Page<Resident> findByEnabledAndCompanyIdAndDeleted(Pageable pageable,Integer state, Long companyId,Integer deleted);
    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwner(Pageable pageable,Integer state, Long companyId,Integer deleted,int owner);
    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseId(Pageable pageable,Integer state, Long companyId,Integer deleted,int owner,Long houseId);
    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndHouseId(Pageable pageable,Integer state, Long companyId,Integer deleted,Long houseId);
    Page<Resident> findByCompanyIdAndDeleted(Pageable pageable,Long companyId,Integer deleted);
    Resident findByCompanyIdAndIdentificationnumberAndDeleted(Long companyId,String identificationNumber,Integer deleted);
    Page<Resident> findByEnabledAndHouseIdAndDeleted(Pageable pageable,Integer state,Long houseId, Integer deleted);
    Integer countByEnabledAndCompanyIdAndDeleted(Integer state,Long companyId,Integer deleted);
}
