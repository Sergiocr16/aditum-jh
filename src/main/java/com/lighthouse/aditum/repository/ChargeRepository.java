package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Charge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


import java.util.List;

import java.time.ZonedDateTime;


/**
 * Spring Data JPA repository for the Charge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChargeRepository extends JpaRepository<Charge, Long> {
    List<Charge> findByHouseIdAndDeletedAndState(Long id, Integer deleted, Integer state);

    List<Charge> findByHouseIdAndDeletedAndStateAndType(Long id, Integer deleted, Integer state,Integer type);


    List<Charge> findByHouseIdAndDeleted(Long id, Integer deleted);

    List<Charge> findByHouseIdAndDeletedAndConcept(Long id, Integer deleted,String concept);


    @Query("select c from Charge c " +
        "where c.date <= ?1 and c.state = ?2 and c.deleted = ?3 and c.house.id = ?4")
    List<Charge> findByHouseBetweenDates(ZonedDateTime finalDate, int state, int deleted, Long houseId);

    @Query("select c from Charge c " +
        "where c.date <= ?1 and c.state = ?2 and c.type =?3 and c.deleted = ?4 and c.house.id = ?5")
    List<Charge> findByHouseBetweenDatesAndType(ZonedDateTime finalDate, int state, int type, int deleted, Long houseId);

    @Query("select c from Charge c " +
        "where c.paymentDate >= ?1 and c.paymentDate <= ?2 and c.type=?3 and c.state = ?4 and c.company.id = ?5")
    List<Charge> findPaidChargesBetweenDatesAndCompanyId(ZonedDateTime initialDate, ZonedDateTime finalDate, int type, int state, Long companyId);

    @Query("select c from Charge c " +
        "where c.paymentDate >= ?1 and c.paymentDate <= ?2 and c.type>?3 and c.state = ?4 and c.company.id = ?5")
    List<Charge> findPaidChargesBetweenDatesAndCompanyIdBeingOther(ZonedDateTime initialDate, ZonedDateTime finalDate, int type, int state, Long companyId);

    @Query("select c from Charge c " +
        "where c.paymentDate >= ?1 and c.paymentDate <= ?2  and c.company.id = ?3")
    List<Charge> findAllChargesBetweenDatesAndCompanyId(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);

    @Query("select c from Charge c " +
        "where c.paymentDate >= ?1 and c.paymentDate <= ?2  and c.house.id = ?3")
    List<Charge> findAllChargesBetweenDatesAndHouseId(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.type=?3 and c.company.id = ?4 and c.deleted=?5 ")
    List<Charge> findBillingReportByType(ZonedDateTime initialDate, ZonedDateTime finalDate, int type, Long companyId, int deleted);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.company.id = ?3 and c.deleted=?4 ")
    List<Charge> findBillingReport(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, int deleted);


    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.type=?3 and c.company.id = ?4 and c.house.id = ?5 and c.deleted=?6 ")
    List<Charge> findBillingReportByTypeAndHouse(ZonedDateTime initialDate, ZonedDateTime finalDate, int type, Long companyId, Long houseId, int deleted);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.company.id = ?3 and c.house.id = ?4  and c.deleted=?5 ")
    List<Charge> findBillingReportAndHouse(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId,Long houseId,  int deleted);




    List<Charge> findByPaymentIdAndDeletedAndState(Long id, Integer deleted, Integer state);


    Charge findByConsecutiveAndDeletedAndStateAndCompanyIdAndHouseId(int consecutive, Integer deleted, Integer state,Long companyId, Long houseId);

    Charge findByConsecutiveAndHouseId(int consecutive,Long houseId);

    Charge findTopByCompanyIdOrderByConsecutiveDesc(Long companyId);

    List<Charge> findAllByConsecutiveAndCompanyId(int consecutive,Long companyId);

    List<Charge> findByConsecutiveAndDeletedAndCompanyIdAndHouseId(int consecutive, Integer deleted,Long companyId, Long houseId);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.house.id = ?3 and c.deleted=?4 order by id desc")
    List<Charge> findAllBetweenDatesAndHouseId(ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, int deleted);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.house.id = ?2 and c.deleted=?3 order by id desc")
    List<Charge> findAllFromDateAndHouseId(ZonedDateTime initialDate, Long houseId, int deleted);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.house.id = ?3 and c.deleted=?4 and c.state =?5 order by id desc")
    List<Charge> findAllBetweenDatesAndHouseIdAndState(ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, int deleted,int state);

    @Query("select c from Charge c " +
        "where  c.date <= ?1 and c.house.id = ?2 and c.deleted=?3 and c.state =?4 order by id desc")
    List<Charge> findAllUntilDatesAndHouseIdAndState(ZonedDateTime finalDate, Long houseId, int deleted,int state);

    @Query("select c from Charge c " +
        "where c.date <= ?1 and c.house.id = ?2 and c.deleted=?3 order by id desc")
    List<Charge> findAllBetweenMorosidadDatesAndHouseId(ZonedDateTime finalDate, Long houseId, int deleted);

    @Query("select c from Charge c " +
        "where c.paymentDate >= ?1 and c.paymentDate <= ?2 and c.house.id = ?3 and c.deleted=?4 order by id desc")
    List<Charge> findAllBetweenPaymentDatesAndHouseId(ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, int deleted);

    @Query("select c from Charge c " +
        "where c.paymentDate <= ?1 and c.house.id = ?2 and c.deleted=?3 order by id desc")
    List<Charge> findAllUntilPaymentDatesAndHouseId( ZonedDateTime finalDate, Long houseId, int deleted);

    @Query("select c from Charge c " +
        "where c.date >= ?1 and c.date <= ?2 and c.house.id = ?3 and c.deleted=?4 and c.type=?5 order by id desc")
    List<Charge> findAllBetweenDatesAndHouseIdAndType(ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, int deleted,int type);

    @Query("select c from Charge c " +
        "where  c.date <= ?1 and c.house.id = ?2 and c.deleted=?3 and c.type=?4 order by id desc")
    List<Charge> findAllBetweenDatesMorosidadAndHouseIdAndType(ZonedDateTime finalDate, Long houseId, int deleted,int type);

    @Query("select c from Charge c " +
        "where c.date <= ?1 and c.house.id = ?2 and c.type= ?3 and c.state = ?4 and c.deleted =?5")
    List<Charge> findBeforeDateAndHouseAndTypeAndStateAndDeleted(ZonedDateTime initialDate, Long houseId, int type, int state, int deleted);

    @Query("select c from Charge c " +
        "where c.date <= ?1 and c.house.id = ?2 and c.type > ?3 and c.state = ?4 and c.deleted =?5")
    List<Charge> findBeforeDateAndHouseAndTypeGreaterThanAndStateAndDeleted(ZonedDateTime initialDate, Long houseId, int type, int state, int deleted);

    @Query("select c from Charge c " +
        "where c.date < ?1 and c.house.id = ?2 and c.deleted = ?3")
    List<Charge> findAllUnderDateAndHouseId(ZonedDateTime initialDate, Long houseId,int deleted);

    Charge findFirstByCompanyIdAndSplitedIsNullOrderByConsecutiveDesc(Long companyId);

    Charge findBySplitedCharge(int splitedCharge);


    List<Charge> findByCompanyId(Long companyId);
}
