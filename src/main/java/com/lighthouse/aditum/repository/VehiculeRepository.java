package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Vehicule;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Vehicule entity.
 */
@SuppressWarnings("unused")
public interface VehiculeRepository extends JpaRepository<Vehicule,Long> {

}
