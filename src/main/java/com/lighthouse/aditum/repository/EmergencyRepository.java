package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Emergency;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Emergency entity.
 */
@SuppressWarnings("unused")
public interface EmergencyRepository extends JpaRepository<Emergency,Long> {

}
