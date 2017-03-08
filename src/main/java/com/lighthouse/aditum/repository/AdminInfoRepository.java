package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AdminInfo;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the AdminInfo entity.
 */
@SuppressWarnings("unused")
public interface AdminInfoRepository extends JpaRepository<AdminInfo,Long> {
    Optional<AdminInfo> findOneByUserId(Long id);
}
