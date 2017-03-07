package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Resident;

import com.lighthouse.aditum.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Resident entity.
 */
@SuppressWarnings("unused")
public interface ResidentRepository extends JpaRepository<Resident,Long> {

    List<Resident> findByEnabledAndCompanyId(Integer state, Long companyId);
}
