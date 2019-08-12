package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.MacroVisit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.time.ZonedDateTime;


/**
 * Spring Data JPA repository for the MacroVisit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MacroVisitRepository extends JpaRepository<MacroVisit, Long> {

    Page<MacroVisit> findByMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndNameContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLastnameContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndSecondlastnameContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIdentificationnumberContainsOrMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLicenseplateContainsOrderByArrivaltimeDesc(
        Pageable pageable, Long macroCondominiumId1, ZonedDateTime initialDate1, ZonedDateTime finalDate1, String name1,
        Long macroCondominiumId2, ZonedDateTime initialDate2, ZonedDateTime finalDate2, String name2,
        Long macroCondominiumId3, ZonedDateTime initialDate3, ZonedDateTime finalDate3, String name3,
        Long macroCondominiumId4, ZonedDateTime initialDate4, ZonedDateTime finalDate4, String name4,
        Long macroCondominiumId5, ZonedDateTime initialDate5, ZonedDateTime finalDate5, String name5);


    Page<MacroVisit> findByCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndNameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndSecondlastnameContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndIdentificationnumberContainsOrCompanyIdAndArrivaltimeAfterAndArrivaltimeBeforeAndLicenseplateContainsOrderByArrivaltimeDesc(
        Pageable pageable, Long companyId1, ZonedDateTime initialDate1, ZonedDateTime finalDate1, String name1,
        Long companyId2, ZonedDateTime initialDate2, ZonedDateTime finalDate2, String name2,
        Long companyId3, ZonedDateTime initialDate3, ZonedDateTime finalDate3, String name3,
        Long companyId4, ZonedDateTime initialDate4, ZonedDateTime finalDate4, String name4,
        Long companyId5, ZonedDateTime initialDate5, ZonedDateTime finalDate5, String name5);


    Page<MacroVisit> findByMacroCondominiumIdAndArrivaltimeAfterAndArrivaltimeBeforeOrderByArrivaltimeDesc(Pageable pageable, Long macroCondominiumId, ZonedDateTime initialDate, ZonedDateTime finalDate);


    Page<MacroVisit> findByArrivaltimeAfterAndArrivaltimeBeforeAndCompanyIdOrderByArrivaltimeDesc(Pageable pageable, ZonedDateTime initialDate, ZonedDateTime finalDate, Long companyId);
    MacroVisit findFirstByMacroCondominiumIdAndLicenseplateOrderByIdDesc(Long macroId,String plate);
    MacroVisit findFirstByMacroCondominiumIdAndIdentificationnumberOrderByIdDesc(Long macroId,String plate);


}
