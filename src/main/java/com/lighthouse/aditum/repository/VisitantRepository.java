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
public interface VisitantRepository extends JpaRepository<Visitant, Long> {
    List<Visitant> findByCompanyId(Long companyId);

    List<Visitant> findByCompanyIdAndIsinvitedOrCompanyIdAndIsinvited(Long companyId, Integer isInvited, Long companyId2, Integer isInvited2);

    Visitant findByIdentificationnumberAndHouseIdAndCompanyId(String identificationNumber, Long houseId, Long companyId);

    List<Visitant> findByCompanyIdAndHouseIdAndIsinvitedOrCompanyIdAndHouseIdAndIsinvited(Long companyId, Long houseId, Integer isInvited, Long companyId2, Long houseId2, Integer isInvited2);

    Integer countByCompanyIdAndIsinvited(Long companyId, Integer isInvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.house.id = ?2 and v.isinvited = ?3")
    List<Visitant> findByHouseInLastMonth(ZonedDateTime firstDayOfMonth, Long houseId, Integer isInvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.company.id = ?2 and v.isinvited = ?3")
    List<Visitant> findForAdminInLastMonth(ZonedDateTime firstDayOfMonth, Long companyId, Integer isInvited);



    Page<Visitant> findByCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedOrderByArrivaltimeDesc(Pageable pageable, Long companyId, ZonedDateTime initialDate, ZonedDateTime finalDate, Integer isinvited);

    Page<Visitant> findByCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndNameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndLastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndSecondlastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndIdentificationnumberContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIsinvitedAndLicenseplateContainsOrderByArrivaltimeDesc(
        Pageable pageable, Long companyId1, ZonedDateTime initialDate1, ZonedDateTime finalDate1, Integer isinvited1, String name1,
        Long companyId2, ZonedDateTime initialDate2, ZonedDateTime finalDate2, Integer isinvited2, String name2,
        Long companyId3, ZonedDateTime initialDate3, ZonedDateTime finalDate3, Integer isinvited3, String name3,
        Long companyId4, ZonedDateTime initialDate4, ZonedDateTime finalDate4, Integer isinvited4, String name4,
        Long companyId5, ZonedDateTime initialDate5, ZonedDateTime finalDate5, Integer isinvited5, String name5);

    Page<Visitant> findByArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedOrderByArrivaltimeDesc(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, Integer isinvited);

    Page<Visitant> findByArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndNameContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndLastnameContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndSecondlastnameContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndIdentificationnumberContainsOrArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvitedAndLicenseplateContainsOrderByArrivaltimeDesc(
        Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, Integer isinvited, String name,
        ZonedDateTime initialDate1, ZonedDateTime finalDate1, Long houseId1, Integer isinvited1, String name1,
        ZonedDateTime initialDate2, ZonedDateTime finalDate2, Long houseId2, Integer isinvited2, String name2,
        ZonedDateTime initialDate3, ZonedDateTime finalDate3, Long houseId3, Integer isinvited3, String name3,
        ZonedDateTime initialDate4, ZonedDateTime finalDate4, Long houseId4, Integer isinvited4, String name4
    );


    List<Visitant> findByArrivaltimeAfterAndArrivaltimeBeforeAndHouseIdAndIsinvited(ZonedDateTime initialDate, ZonedDateTime finalDate, Long houseId, Integer isinvited);

    List<Visitant> findByArrivaltimeAfterAndArrivaltimeBeforeAndCompanyIdAndIsinvited(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, Integer isinvited);



    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.company.id = ?2 and v.isinvited = ?3")
    List<Visitant> findByCompanyInLastMonth(ZonedDateTime firstDayOfMonth, Long companyId, Integer isInvited);

    @Query("select count(v) from Visitant v " +
        "where v.arrivaltime >= ?1 and v.company.id = ?2 and v.isinvited = ?3")
    Integer countByCompanyInLastMonth(ZonedDateTime firstDayOfMonth, Long companyId, Integer isInvited);

    @Query("select count(v) from Visitant v " +
        "where v.arrivaltime >= ?1 and v.arrivaltime <= ?2 and v.company.id = ?3 and v.isinvited = ?4")
    Integer countByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, Integer isinvited);

    @Query("select v from Visitant v " +
        "where v.arrivaltime >= ?1 and v.arrivaltime <= ?2 and v.company.id = ?3 and v.isinvited = ?4")
    List<Visitant> findByDatesBetweenAndCompany(ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId, Integer isinvited);
}
