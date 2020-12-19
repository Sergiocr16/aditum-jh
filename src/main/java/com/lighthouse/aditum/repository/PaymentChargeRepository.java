package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.PaymentCharge;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the PaymentCharge entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentChargeRepository extends JpaRepository<PaymentCharge, Long> {
    List<PaymentCharge> findAllByPaymentId(Long paymentId);

    List<PaymentCharge> findAllByOriginalCharge(Long originalCharge);

}
