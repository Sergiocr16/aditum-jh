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
    Officer findOneByUserId(Long id);
    Page<Officer> findByCompanyId(Pageable pageable, Long companyId);
    Officer findByCompanyIdAndIdentificationnumber(Long companyId,String identificationNumber);
    Integer countByCompanyId(Long companyId);
//    List<Officer> findByEnabledAndCompanyId(Integer state, Long companyId);

    List<Officer> findByEnableAndCompanyId(boolean state, Long companyId);

}
