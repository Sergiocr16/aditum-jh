package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Transferencia;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Transferencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia,Long> {
    
}
