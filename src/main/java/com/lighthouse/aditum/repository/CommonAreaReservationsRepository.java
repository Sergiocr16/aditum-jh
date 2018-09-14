package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.CommonAreaReservations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * Spring Data JPA repository for the CommonAreaReservations entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommonAreaReservationsRepository extends JpaRepository<CommonAreaReservations,Long> {

    @Query("select e from CommonAreaReservations e " +
        "where e.initalDate >= ?1 and e.initalDate <= ?2 and e.commonArea.id = ?3 and e.status  <3")
    List<CommonAreaReservations> findByBetweenDatesAndCommonArea(ZonedDateTime zd_reservation_initial_date, ZonedDateTime zd_reservation_final_date, Long common_area_id);

    @Query("select e from CommonAreaReservations e " +
        "where e.initalDate >= ?1 and e.initalDate < ?2 and e.commonArea.id = ?3 and e.status  <3")
    List<CommonAreaReservations> findReservationBetweenIT(ZonedDateTime zd_reservation_initial_date, ZonedDateTime zd_reservation_final_date, Long common_area_id);

    @Query("select e from CommonAreaReservations e " +
        "where e.finalDate > ?1 and e.finalDate < ?2 and e.commonArea.id = ?3 and e.status  <3")
    List<CommonAreaReservations> findReservationBetweenFT(ZonedDateTime zd_reservation_initial_date, ZonedDateTime zd_reservation_final_date, Long common_area_id);

    @Query("select e from CommonAreaReservations e " +
        "where e.initalDate < ?1 and e.finalDate > ?1 and e.commonArea.id = ?2 and e.status <3 ")
    List<CommonAreaReservations> findReservationInIT(ZonedDateTime zd_reservation_initial_date, Long common_area_id);

    Page<CommonAreaReservations> findByCompanyIdAndStatus(Pageable pageable, Long companyId, int status);

    Page<CommonAreaReservations> findByCompanyIdAndStatusNot(Pageable pageable, Long companyId, int status);

    @Query("select e from CommonAreaReservations e " +
        "where e.commonArea.id = ?1 and e.status <3 ")
    Page<CommonAreaReservations> findByCommonAreaIdAndStatus(Pageable pageable, Long commonAreaId);

    @Query("select e from CommonAreaReservations e " +
        "where e.company.id = ?1 and e.status <3 ")
    Page<CommonAreaReservations> findgetPendingAndAcceptedReservations(Pageable pageable, Long companyId);
//    @Query("select e from CommonAreaReservations e " +
//        "where e.company.id < ?2  and e.status <>4")
//    Page<CommonAreaReservations> findAllByCompanyId(Pageable pageable, Long companyId);

}
