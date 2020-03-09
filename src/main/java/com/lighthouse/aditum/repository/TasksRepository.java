package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Tasks entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long> {

    Page<Tasks> findAllByCompanyIdAndStatusAndDeleted(Pageable pageable, Long companyId, Integer status, Integer deleted);


}
