package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.JuntaDirectivaAccount;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the JuntaDirectivaAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JuntaDirectivaAccountRepository extends JpaRepository<JuntaDirectivaAccount, Long> {
    JuntaDirectivaAccount findOneByUserId(Long id);
    JuntaDirectivaAccount findByCompanyId(Long companyId);
}
