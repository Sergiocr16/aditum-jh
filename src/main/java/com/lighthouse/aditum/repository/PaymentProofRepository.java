package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.PaymentProof;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the PaymentProof entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentProofRepository extends JpaRepository<PaymentProof, Long> {

}
