package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Visitant;
import com.lighthouse.aditum.domain.VisitantInvitation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the VisitantInvitation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitantInvitationRepository extends JpaRepository<VisitantInvitation, Long> {
    List<VisitantInvitation> findByCompanyIdAndHouseIdAndStatusAndHasscheduleOrCompanyIdAndHouseIdAndStatusAndHasschedule(Long companyId, Long houseId, Integer status, Integer hasschedule, Long companyId2, Long houseId2, Integer status2, Integer hasschedule2);

    VisitantInvitation findByIdentificationnumberAndHouseIdAndCompanyIdAndHasschedule(String identificationNumber, Long houseId, Long companyId, Integer hasschedule);

    List<VisitantInvitation> findByCompanyIdAndStatus(Long companyId, Integer status);

    @Query("select e from VisitantInvitation e " +
        "where e.companyId = ?1 and e.status =?2 and e.houseId =?4 and (e.name LIKE CONCAT('%',UPPER(?3),'%')  or e.lastname LIKE CONCAT('%',UPPER(?3),'%')  or e.secondlastname LIKE CONCAT('%',UPPER(?3),'%')  or e.identificationnumber LIKE CONCAT('%',UPPER(?3),'%') or e.licenseplate LIKE CONCAT('%',UPPER(?3),'%') ) order by e.name desc")
    Page<VisitantInvitation> findByCompanyIdAndStatusAndFilterAndHouseId(
        Long companyId, Integer status,String name, long houseId,Pageable pageable);

    @Query("select e from VisitantInvitation e " +
        "where e.companyId = ?1 and e.status =?2 and (e.name LIKE CONCAT('%',UPPER(?3),'%')  or e.lastname LIKE CONCAT('%',UPPER(?3),'%')  or e.secondlastname LIKE CONCAT('%',UPPER(?3),'%')  or e.identificationnumber LIKE CONCAT('%',UPPER(?3),'%') or e.licenseplate LIKE CONCAT('%',UPPER(?3),'%')  ) order by e.name desc")
    Page<VisitantInvitation> findByCompanyIdAndStatusAndFilter(
        Long companyId, Integer status,String name,Pageable pageable);

    Page<VisitantInvitation> findByCompanyIdAndHouseIdAndStatus(
        Pageable pageable, Long companyId,Long houseId, Integer status);

    Page<VisitantInvitation> findByCompanyIdAndStatus(
        Pageable pageable, Long companyId, Integer status);



    List<VisitantInvitation> findByHouseIdAndStatus(Long houseId, Integer status);

    List<VisitantInvitation> findByCompanyIdAndStatusAndHasscheduleOrCompanyIdAndStatusAndHasschedule(Long companyId, Integer status, Integer hasschedule, Long companyId2, Integer status2, Integer hasschedule2);

    List<VisitantInvitation> findByStatusAndIdentificationnumberAndCompanyIdIn(
        Integer status, String identification, List<Long> companiesId);

    List<VisitantInvitation> findByStatusAndIdentificationnumberAndCompanyId(Integer status, String identification, Long companyId);

    List<VisitantInvitation> findByStatusAndLicenseplateAndCompanyIdIn(
        Integer status, String plate, List<Long> companiesId);

    List<VisitantInvitation> findByStatusAndLicenseplateAndCompanyId(
        Integer status, String plate, Long companyId);
}
