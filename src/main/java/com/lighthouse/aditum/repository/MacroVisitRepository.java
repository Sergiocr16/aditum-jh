package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.MacroVisit;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MacroVisit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MacroVisitRepository extends JpaRepository<MacroVisit, Long> {
    MacroVisit findFirstByMacroCondominiumIdAndLicenseplateOrderByIdDesc(Long macroId,String plate);
    MacroVisit findFirstByMacroCondominiumIdAndIdentificationnumberOrderByIdDesc(Long macroId,String plate);


}
