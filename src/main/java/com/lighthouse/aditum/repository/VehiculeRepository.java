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
    Page<Vehicule> findByCompanyId(Pageable pageable, Long companyId);
    Vehicule findOneByCompanyIdAndLicenseplate(Long companyId,String licensePlate);
    List<Vehicule> findByEnabledAndCompanyId(Integer state, Long companyId);
    List<Vehicule> findByEnabledAndHouseId(Integer state,Long houseId);
    Integer countByEnabledAndCompanyId(Integer state,Long companyId);
}
