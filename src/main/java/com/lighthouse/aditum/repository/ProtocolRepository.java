package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Protocol;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Protocol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProtocolRepository extends JpaRepository<Protocol, Long> {

}
