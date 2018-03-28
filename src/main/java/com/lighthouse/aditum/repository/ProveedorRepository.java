package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Proveedor;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Proveedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor,Long> {
    List<Proveedor> findByCompanyId(Long companyId);
}
