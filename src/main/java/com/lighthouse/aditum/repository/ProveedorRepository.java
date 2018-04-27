package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Proveedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor,Long> {
    Page<Proveedor> findByDeletedAndCompanyId(Pageable pageable, Integer deleted, Long companyId);
}
