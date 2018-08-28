package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CommonAreaSchedule;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the CommonAreaSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommonAreaScheduleRepository extends JpaRepository<CommonAreaSchedule,Long> {
    List<CommonAreaSchedule> findByCommonAreaId(Long commonAreaId);
}
