package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ActivosFijos;
import com.lighthouse.aditum.domain.Banco;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ActivosFijos entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivosFijosRepository extends JpaRepository<ActivosFijos, Long> {
    Page<ActivosFijos> findByCompanyIdAndDeleted(Pageable pageable, Long companyId, int deleted);
}
