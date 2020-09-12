package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.MensualBillingFile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the MensualBillingFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MensualBillingFileRepository extends JpaRepository<MensualBillingFile, Long> {

    List<MensualBillingFile> findAllByCompanyIdAndMonthAndYearAndStatusAndDeleted(Long companyId, String month, String year, String status, int deleted);

    List<MensualBillingFile> findAllByCompanyIdAndMonthAndYearAndDeleted(Long companyId, String month, String year, int deleted);


}
