package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Visitant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the Visitant entity.
 */
@SuppressWarnings("unused")
public interface VisitantRepository extends JpaRepository<Visitant,Long> {
    Page<Visitant> findByCompanyId(Pageable pageable, Long companyId);
    Visitant findByIdentificationnumberAndHouseIdAndCompanyId(String identificationNumber, Long houseId, Long companyId );
    List<Visitant> findByCompanyIdAndHouseIdAndIsinvitedOrIsinvited(Long companyId, Long houseId,Integer isInvited,Integer isInvited2);
    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.house.id = ?2 and v.isinvited = ?3")
    List<Visitant> findByHouseInLastMonth(ZonedDateTime firstDayOfMonth, Long houseId, Integer isInvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.arrivaltime <= ?2 and v.house.id = ?3 and v.isinvited = ?4")
    List<Visitant> findByDatesBetweenAndHouse(ZonedDateTime initialDate, ZonedDateTime finalDate,Long houseId,Integer isinvited);
}
