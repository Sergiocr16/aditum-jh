package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Transferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the Transferencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia,Long> {
    @Query("select e from Transferencia e " +
        "where e.fecha >= ?1 and e.fecha <= ?2 and e.idBancoDestino = ?3")
    Page<Transferencia> findBetweenDatesByInComingTransfer(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate,  int accountId);

    @Query("select e from Transferencia e " +
        "where e.fecha >= ?1 and e.fecha <= ?2 and e.idBancoOrigen = ?3")
    Page<Transferencia> findBetweenDatesByOutgoingTransfer(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate,  int accountId);

    @Query("select e from Transferencia e " +
        "where e.fecha >= ?1 and e.fecha <= ?2 and e.idBancoDestino = ?3")
    List<Transferencia> findBetweenDatesByInComingTransfer(ZonedDateTime initialDate, ZonedDateTime finalDate,  int accountId);

    @Query("select e from Transferencia e " +
        "where e.fecha >= ?1 and e.fecha <= ?2 and e.idBancoOrigen = ?3")
    List<Transferencia> findBetweenDatesByOutgoingTransfer(ZonedDateTime initialDate, ZonedDateTime finalDate, int accountId);

}
