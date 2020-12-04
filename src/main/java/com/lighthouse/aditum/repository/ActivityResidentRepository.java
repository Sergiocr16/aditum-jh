package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.ActivityResident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the ActivityResident entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivityResidentRepository extends JpaRepository<ActivityResident, Long> {

    Page<ActivityResident> findAllByUser(Pageable pageable, Long user);

    Page<ActivityResident> findTopByUserAndSeen(Pageable pageable, Long user, int seen);


}
