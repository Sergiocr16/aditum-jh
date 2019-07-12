package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Vehicule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vehicule entity.
 */
@SuppressWarnings("unused")
public interface VehiculeRepository extends JpaRepository<Vehicule,Long> {
    List<Vehicule> findByCompanyIdAndDeleted(Long companyId,Integer deleted);
    Vehicule findOneByCompanyIdAndLicenseplateAndDeleted(Long companyId,String licensePlate,Integer deleted);
    Page<Vehicule> findByEnabledAndCompanyIdAndDeleted(Pageable pageable,Integer state, Long companyId,Integer deleted);
    Page<Vehicule> findByEnabledAndCompanyIdAndDeletedAndLicenseplateContains(Pageable pageable,Integer state, Long companyId,Integer deleted,String licensePlate);
    Page<Vehicule> findByEnabledAndCompanyIdInAndDeletedAndLicenseplateContains(Pageable pageable,Integer state,List<Long> companiesId, Integer deleted,String licensePlate);
    Page<Vehicule> findByEnabledAndCompanyIdAndDeletedAndHouseId(Pageable pageable,Integer state, Long companyId,Integer deleted,Long houseId);
    Page<Vehicule> findByEnabledAndCompanyIdAndDeletedAndHouseIdAndLicenseplateContains(Pageable pageable,Integer state, Long companyId,Integer deleted,Long houseId,String licensePlate);
    List<Vehicule> findByEnabledAndCompanyIdAndDeleted(Integer state, Long companyId,Integer deleted);
    List<Vehicule> findByEnabledAndHouseIdAndDeleted(Integer state,Long houseId,Integer deleted);
    Integer countByEnabledAndCompanyIdAndDeleted(Integer state,Long companyId,Integer deleted);
}
