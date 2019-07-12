package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.MacroAdminAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MacroAdminAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MacroAdminAccountRepository extends JpaRepository<MacroAdminAccount, Long> {
    MacroAdminAccount findOneByUserId(Long id);
    Page<MacroAdminAccount> findByMacroCondominiumId(Pageable pageable, Long id);
}
