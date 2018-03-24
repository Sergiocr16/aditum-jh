package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Banco;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Banco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BancoRepository extends JpaRepository<Banco,Long> {
    
}
