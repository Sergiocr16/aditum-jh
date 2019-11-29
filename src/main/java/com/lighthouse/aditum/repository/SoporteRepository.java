package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Soporte;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Soporte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SoporteRepository extends JpaRepository<Soporte, Long> {

}
