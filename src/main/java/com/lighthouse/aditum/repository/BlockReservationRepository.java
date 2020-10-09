package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.BlockReservation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the BlockReservation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BlockReservationRepository extends JpaRepository<BlockReservation, Long> {

    BlockReservation findOneByHouseId(Long houseId);

}
