package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Resident;

import com.lighthouse.aditum.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Resident entity.
 */
@SuppressWarnings("unused")
public interface ResidentRepository extends JpaRepository<Resident, Long> {
    Resident findOneByUserId(Long id);

    Page<Resident> findByEnabledAndCompanyIdAndDeleted(Pageable pageable, Integer state, Long companyId, Integer deleted);

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndNameContainsOrEnabledAndCompanyIdAndDeletedAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndIdentificationnumberContains(Pageable pageable, Integer state, Long companyId, Integer deleted, String name,
                                                                                                                                                                                                                                              Integer state1, Long companyId1, Integer deleted1, String name1,
                                                                                                                                                                                                                                              Integer state2, Long companyId2, Integer deleted2, String name2,
                                                                                                                                                                                                                                              Integer state3, Long companyId3, Integer deleted3, String name3);

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwner(Pageable pageable, Integer state, Long companyId, Integer deleted, int owner);


    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndNameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndIdentificationnumberContains(
        Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, String name,
        Integer state1, Long companyId1, Integer deleted1, int owner1, String name1,
        Integer state2, Long companyId2, Integer deleted2, int owner2, String name2,
        Integer state3, Long companyId3, Integer deleted3, int owner3, String name3
    );

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseId(Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, Long houseId);


    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndNameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndIdentificationnumberContains(
        Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, Long houseId, String name,
        Integer state1, Long companyId1, Integer deleted1, int owner1, Long houseId1, String name1,
        Integer state2, Long companyId2, Integer deleted2, int owner2, Long houseId2, String name2,
        Integer state3, Long companyId3, Integer deleted3, int owner3, Long houseId3, String name3
    );

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndHouseId(Pageable pageable, Integer state, Long companyId, Integer deleted, Long houseId);


    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndHouseIdAndNameContainsOrEnabledAndCompanyIdAndDeletedAndHouseIdAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndHouseIdAndSecondlastnameContainsOrEnabledAndCompanyIdAndDeletedAndHouseIdAndIdentificationnumberContains(
        Pageable pageable, Integer state, Long companyId, Integer deleted, Long houseId, String name
        , Integer state1, Long companyId1, Integer deleted1, Long houseId1, String name1
        , Integer state2, Long companyId2, Integer deleted2, Long houseId2, String name2
        , Integer state3, Long companyId3, Integer deleted3, Long houseId3, String name3);

    Page<Resident> findByEnabledAndDeletedAndNameContainsAndCompanyIdInOrEnabledAndDeletedAndLastnameContainsAndCompanyIdInOrEnabledAndDeletedAndSecondlastnameContainsAndCompanyIdInOrEnabledAndDeletedAndIdentificationnumberContainsAndCompanyIdIn(
        Pageable pageable,
        Integer enabled, Integer deleted,String filter,List<Long> companiesId,
        Integer enabled1, Integer deleted1,String filter1,List<Long> companiesId1,
        Integer enabled2, Integer deleted2,String filter2,List<Long> companiesId2,
        Integer enabled3, Integer deleted3,String filter3,List<Long> companiesId3);

    Resident findByEnabledAndDeletedAndIdentificationnumberAndCompanyIdIn(
        Integer enabled, Integer deleted,String filter,List<Long> companiesId);
    Resident findByEnabledAndDeletedAndIdentificationnumberAndCompanyId(Integer enabled, Integer deleted,String filter,Long companyId);
    List<Resident> findByCompanyIdAndDeleted(Long companyId, Integer deleted);
    List<Resident> findByPrincipalContactAndCompanyIdAndDeleted(Integer principalContact,Long houseId, Integer deleted);
    Resident findByCompanyIdAndIdentificationnumberAndDeleted(Long companyId, String identificationNumber, Integer deleted);

    Page<Resident> findByEnabledAndHouseIdAndDeleted(Pageable pageable, Integer state, Long houseId, Integer deleted);

    Integer countByEnabledAndCompanyIdAndDeleted(Integer state, Long companyId, Integer deleted);
}
