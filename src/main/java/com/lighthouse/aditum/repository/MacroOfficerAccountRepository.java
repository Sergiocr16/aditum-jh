package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.MacroOfficerAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the MacroOfficerAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MacroOfficerAccountRepository extends JpaRepository<MacroOfficerAccount, Long> {
    Page<MacroOfficerAccount> findByMacroCondominiumId(Pageable pageable, Long id);
    MacroOfficerAccount findOneByUserId(Long id);


}
