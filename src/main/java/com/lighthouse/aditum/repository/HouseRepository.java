package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.House;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the House entity.
 */
@SuppressWarnings("unused")
public interface HouseRepository extends JpaRepository<House,Long> {
    Page<House> findByCompanyId(Pageable pageable, Long companyId);
}
