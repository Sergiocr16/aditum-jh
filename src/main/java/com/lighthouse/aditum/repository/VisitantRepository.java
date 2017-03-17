package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Visitant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Visitant entity.
 */
@SuppressWarnings("unused")
public interface VisitantRepository extends JpaRepository<Visitant,Long> {
    Page<Visitant> findByCompanyId(Pageable pageable, Long companyId);
}
