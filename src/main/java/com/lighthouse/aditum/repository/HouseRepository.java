package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.House;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Data JPA repository for the House entity.
 */
@SuppressWarnings("unused")
public interface HouseRepository extends JpaRepository<House, Long> {
    ArrayList<House> findByCompanyId(Long companyId);

    Page<House> findByCompanyId(Pageable pageable, Long companyId);
    Page<House> findByCompanyIdAndHousenumberContains(Pageable pageable, Long companyId,String houseNumber);
    Page<House> findByCompanyIdAndIsdesocupated(Pageable page, Long companyId, int desocupated);
    Page<House> findByCompanyIdAndIsdesocupatedAndHousenumberContains(Pageable page, Long companyId, int desocupated,String houseNumber);


    House findByhousenumberAndCompanyId(String houseNumber, Long companyId);

    House findByExtensionAndCompanyId(String extension, Long companyId);

    Integer countByCompanyIdAndIsdesocupated(Long companyId, Integer isDesocupated);

    Integer countByCompanyId(Long companyId);

    House findByLoginCode(String loginCode);

    House findByhousenumberAndAndCompanyIdAndIdNot(String extension, Long companyId, Long houseId);

    House findByExtensionAndCompanyIdAndIdNot(String extension, Long companyId, Long houseId);


    @Query("select h.housenumber from House h " +
        "where h.id = ?1")
    String findHouseNumber(Long houseId);



}
