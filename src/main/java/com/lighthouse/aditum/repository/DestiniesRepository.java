package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Destinies;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Destinies entity.
 */
@SuppressWarnings("unused")
public interface DestiniesRepository extends JpaRepository<Destinies,Long> {

}
