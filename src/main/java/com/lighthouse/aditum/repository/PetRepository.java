package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Pet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Pet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    Page<Pet> findAllByCompanyIdAndDeleted(Pageable page, Long companyId,int deleted);

    Page<Pet> findAllByCompanyIdAndNameContainsAndDeleted(Pageable page, Long companyId, String name,int deleted);

    Page<Pet> findAllByHouseIdAndDeleted(Pageable page, Long companyId, int deleted);

    Page<Pet> findAllByHouseIdAndNameContainsAndDeleted(Pageable page, Long companyId, String name, int deleted);

}
