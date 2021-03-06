package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.BitacoraAcciones;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BitacoraAcciones entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BitacoraAccionesRepository extends JpaRepository<BitacoraAcciones, Long> {
    Page<BitacoraAcciones> findByCompanyId(Pageable pageable, Long companyId);
    Page<BitacoraAcciones> findByCompanyIdAndType(Pageable pageable, Long companyId,int type);
}
