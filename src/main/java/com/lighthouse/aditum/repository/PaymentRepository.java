package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    @Query("select e from Payment e " +
        "where e.date >= ?1 and e.date <= ?2 and e.companyId = ?3")
    Page<Payment> findByDatesBetweenAndCompany(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate, int companyId);
    @Query("select e from Payment e " +
        "where e.date >= ?1 and e.date <= ?2 and e.companyId = ?3 and e.account = ?4")
    Page<Payment> findByDatesBetweenAndCompanyAndAccount(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate, int companyId,String accountId);
    @Query("select e from Payment e " +
        "where e.date >= ?1 and e.date <= ?2 and e.companyId = ?3 and e.account = ?4")
    List<Payment> findByDatesBetweenAndCompanyAndAccount(ZonedDateTime initialDate, ZonedDateTime finalDate, int companyId,String accountId);
    @Query("select e from Payment e " +
        "where e.date >= ?1 and e.date <= ?2 and e.companyId = ?3 and e.transaction = ?4")
    List<Payment> findAdelantosByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, int companyId,String transaction);

    @Query("select e from Payment e " +
        "where e.transaction = ?1 and e.ammountLeft > ?2 and e.house.id = ?3")
    Page<Payment> findPaymentsInAdvance(Pageable pageable,String transaction, String ammountLeft, Long houseId);

    Page<Payment> findByHouseId(Pageable pageable,Long houseId);

    @Query("select e from Payment e " +
        "where e.date >= ?1 and e.date <= ?2 and e.house.id = ?3")
    Page<Payment> findByDatesBetweenAndHouseId(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate,Long houseId);


}
