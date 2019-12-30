package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.PaymentProof;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentProof entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentProofRepository extends JpaRepository<PaymentProof, Long> {
    Page<PaymentProof> findByHouseIdAndStatus(Pageable pageable, Long houseId, Integer status);
    Page<PaymentProof> findByHouseIdAndPaymentId(Pageable pageable, Long houseId,  Long paymentId);
    Page<PaymentProof> findByPaymentId(Pageable pageable, Long paymentId);
    Page<PaymentProof> findByCompanyIdAndStatus(Pageable pageable, Long companyId, Integer status);
}
