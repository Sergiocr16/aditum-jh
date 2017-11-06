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
    List<Visitant> findByCompanyId(Long companyId);
    List<Visitant> findByCompanyIdAndIsinvitedOrCompanyIdAndIsinvited(Long companyId,Integer isInvited,Long companyId2, Integer isInvited2);

    Visitant findByIdentificationnumberAndHouseIdAndCompanyId(String identificationNumber, Long houseId, Long companyId );
    List<Visitant> findByCompanyIdAndHouseIdAndIsinvitedOrCompanyIdAndHouseIdAndIsinvited(Long companyId, Long houseId,Integer isInvited, Long companyId2, Long houseId2, Integer isInvited2);

    Integer countByCompanyIdAndIsinvited(Long companyId,Integer isInvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.house.id = ?2 and v.isinvited = ?3")
    List<Visitant> findByHouseInLastMonth(ZonedDateTime firstDayOfMonth, Long houseId, Integer isInvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.arrivaltime <= ?2 and v.house.id = ?3 and v.isinvited = ?4")
    List<Visitant> findByDatesBetweenAndHouse(ZonedDateTime initialDate, ZonedDateTime finalDate,Long houseId,Integer isinvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.company.id = ?2 and v.isinvited = ?3")
    List<Visitant> findByCompanyInLastMonth(ZonedDateTime firstDayOfMonth, Long companyId, Integer isInvited);

    @Query("select count(v) from Visitant v " +
        "where v.arrivaltime >= ?1 and v.company.id = ?2 and v.isinvited = ?3")
    Integer countByCompanyInLastMonth(ZonedDateTime firstDayOfMonth, Long companyId, Integer isInvited);

    @Query("select count(v) from Visitant v " +
        "where v.arrivaltime >= ?1 and v.arrivaltime <= ?2 and v.company.id = ?3 and v.isinvited = ?4")
    Integer countByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate,Long companyId,Integer isinvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.arrivaltime <= ?2 and v.company.id = ?3 and v.isinvited = ?4")
    List<Visitant> findByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate,Long companyId,Integer isinvited);
}
