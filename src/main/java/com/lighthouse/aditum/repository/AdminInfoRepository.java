package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AdminInfo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the AdminInfo entity.
 */
@SuppressWarnings("unused")
public interface AdminInfoRepository extends JpaRepository<AdminInfo,Long> {
    AdminInfo findOneByUserId(Long id);
    Page<AdminInfo> findByCompanyId(Pageable pageable, Long companyId);
}
