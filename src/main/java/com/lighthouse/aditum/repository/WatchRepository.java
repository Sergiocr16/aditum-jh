package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Watch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Watch entity.
 */
@SuppressWarnings("unused")
public interface WatchRepository extends JpaRepository<Watch,Long> {
    Page<Watch> findByCompanyId(Pageable pageable, Long companyId);
}
