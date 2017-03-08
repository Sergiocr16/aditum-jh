package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Resident;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Resident entity.
 */
@SuppressWarnings("unused")
public interface ResidentRepository extends JpaRepository<Resident,Long> {
    Optional<Resident> findOneByUserId(Long id);

//    @Query("select u.companyId from User u inner join u.area ar where ar.idArea = :idArea")
//
}
