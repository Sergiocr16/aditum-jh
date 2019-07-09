package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Visitant;
import com.lighthouse.aditum.domain.VisitantInvitation;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the VisitantInvitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitantInvitationRepository extends JpaRepository<VisitantInvitation, Long> {
    List<VisitantInvitation> findByCompanyIdAndHouseIdAndStatusAndHasscheduleOrCompanyIdAndHouseIdAndStatusAndHasschedule(Long companyId, Long houseId, Integer status, Integer hasschedule,Long companyId2, Long houseId2, Integer status2,Integer hasschedule2);
    VisitantInvitation findByIdentificationnumberAndHouseIdAndCompanyIdAndHasschedule(String identificationNumber, Long houseId, Long companyId, Integer hasschedule);

}
