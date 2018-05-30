package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Officer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Officer entity.
 */
@SuppressWarnings("unused")
public interface OfficerRepository extends JpaRepository<Officer,Long> {

    Page<Officer> findByCompanyIdAndDeleted(Pageable pageable, Long companyId,Integer deleted);
    Officer findByCompanyIdAndIdentificationnumberAndDeleted(Long companyId,String identificationNumber, Integer deleted);
    Integer countByCompanyIdAndDeleted(Long companyId, Integer deleted);
//    List<Officer> findByEnabledAndCompanyId(Integer state, Long companyId);

    List<Officer> findByEnableAndCompanyIdAndDeleted(boolean state, Long companyId, Integer deleted);

}
