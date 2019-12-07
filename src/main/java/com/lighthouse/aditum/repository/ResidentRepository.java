package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.House;
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

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndTypeNot(Pageable pageable, Integer state, Long companyId, Integer deleted, Integer type);

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndLastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIdentificationnumberContainsAndTypeNot(Pageable pageable, Integer state, Long companyId, Integer deleted, String name, Integer type,
                                                                                                                                                                                                                                                                                      Integer state1, Long companyId1, Integer deleted1, String name1, Integer type2,
                                                                                                                                                                                                                                                                                      Integer state2, Long companyId2, Integer deleted2, String name2, Integer type3,
                                                                                                                                                                                                                                                                                      Integer state3, Long companyId3, Integer deleted3, String name3, Integer type4);

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndTypeNot(Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, Integer type);


    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndLastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndIdentificationnumberContainsAndTypeNot(
        Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, String name, Integer type,
        Integer state1, Long companyId1, Integer deleted1, int owner1, String name1, Integer type1,
        Integer state2, Long companyId2, Integer deleted2, int owner2, String name2, Integer type2,
        Integer state3, Long companyId3, Integer deleted3, int owner3, String name3, Integer type3
    );

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndTypeNot(Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, Long houseId, Integer type);


    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndLastnameContainsOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndIsOwnerAndHouseIdAndIdentificationnumberContainsAndTypeNot(
        Pageable pageable, Integer state, Long companyId, Integer deleted, int owner, Long houseId, String name, Integer type,
        Integer state1, Long companyId1, Integer deleted1, int owner1, Long houseId1, String name1, Integer type1,
        Integer state2, Long companyId2, Integer deleted2, int owner2, Long houseId2, String name2, Integer type2,
        Integer state3, Long companyId3, Integer deleted3, int owner3, Long houseId3, String name3, Integer type3
    );

    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndHouseIdAndTypeNot(Pageable pageable, Integer state, Long companyId, Integer deleted, Long houseId, Integer type);


    Page<Resident> findByEnabledAndCompanyIdAndDeletedAndHouseIdAndNameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndHouseIdAndLastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndHouseIdAndSecondlastnameContainsAndTypeNotOrEnabledAndCompanyIdAndDeletedAndHouseIdAndIdentificationnumberContainsAndTypeNot(
        Pageable pageable, Integer state, Long companyId, Integer deleted, Long houseId, String name, Integer type
        , Integer state1, Long companyId1, Integer deleted1, Long houseId1, String name1, Integer type1
        , Integer state2, Long companyId2, Integer deleted2, Long houseId2, String name2, Integer type2
        , Integer state3, Long companyId3, Integer deleted3, Long houseId3, String name3, Integer type3);

    Page<Resident> findByEnabledAndDeletedAndNameContainsAndCompanyIdInOrEnabledAndDeletedAndLastnameContainsAndCompanyIdInOrEnabledAndDeletedAndSecondlastnameContainsAndCompanyIdInOrEnabledAndDeletedAndIdentificationnumberContainsAndCompanyIdIn(
        Pageable pageable,
        Integer enabled, Integer deleted, String filter, List<Long> companiesId,
        Integer enabled1, Integer deleted1, String filter1, List<Long> companiesId1,
        Integer enabled2, Integer deleted2, String filter2, List<Long> companiesId2,
        Integer enabled3, Integer deleted3, String filter3, List<Long> companiesId3);

    Resident findByEnabledAndDeletedAndIdentificationnumberAndCompanyIdIn(
        Integer enabled, Integer deleted, String filter, List<Long> companiesId);

    Resident findByDeletedAndIdentificationnumberAndCompanyIdAndTypeNot(Integer deleted, String filter, Long companyId, Integer type);

    List<Resident> findByCompanyIdAndDeleted(Long companyId, Integer deleted);

    List<Resident> findByPrincipalContactAndCompanyIdAndDeleted(Integer principalContact, Long houseId, Integer deleted);

    Resident findByCompanyIdAndIdentificationnumberAndDeleted(Long companyId, String identificationNumber, Integer deleted);

    Page<Resident> findByEnabledAndHouseIdAndDeleted(Pageable pageable, Integer state, Long houseId, Integer deleted);

    Integer countByEnabledAndCompanyIdAndDeleted(Integer state, Long companyId, Integer deleted);

    //    OWNER

    List<Resident> findByHouses(List<House> housesId);

    List<Resident> findByHouseIdAndTypeIsLessThan(Long houseId,Integer type);


    Page<Resident> findByTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndNameContainsOrTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndLastnameContainsOrTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndSecondlastnameContainsOrTypeIsLessThanAndCompanyIdAndHousesAndDeletedAndIdentificationnumberContains(
        Pageable pageable, Integer state, Long companyId, List<House> houseId, Integer deleted, String name
        , Integer state1, Long companyId1, List<House> houseId1, Integer deleted1, String name1
        , Integer state2, Long companyId2, List<House> houseId2, Integer deleted2, String name2
        , Integer state3, Long companyId3, List<House> houseId3, Integer deleted3, String name3
    );

    Page<Resident> findByTypeLessThanAndCompanyIdAndDeletedAndNameContainsOrTypeIsLessThanAndCompanyIdAndDeletedAndLastnameContainsOrTypeIsLessThanAndCompanyIdAndDeletedAndSecondlastnameContainsOrTypeIsLessThanAndCompanyIdAndDeletedAndIdentificationnumberContains(
        Pageable pageable, Integer state, Long companyId, Integer deleted, String name
        , Integer state1, Long companyId1, Integer deleted1, String name1
        , Integer state2, Long companyId2, Integer deleted2, String name2
        , Integer state3, Long companyId3, Integer deleted3, String name3
    );

    Page<Resident> findByTypeLessThanAndCompanyIdAndHousesAndDeleted(
        Pageable pageable, Integer state, Long companyId, List<House> houseId, Integer deleted
    );

    Page<Resident> findByTypeLessThanAndCompanyIdAndDeleted(
        Pageable pageable, Integer state, Long companyId, Integer deleted
    );
}

