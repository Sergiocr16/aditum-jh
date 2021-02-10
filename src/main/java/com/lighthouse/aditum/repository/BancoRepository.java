package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Banco;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Banco entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BancoRepository extends JpaRepository<Banco,Long> {
    Page<Banco> findByCompanyIdAndDeleted(Pageable pageable, Long companyId,int deleted);
    List<Banco> findByCompanyIdAndDeleted( Long companyId,int deleted);
    List<Banco> findByCompanyIdAndBeneficiario( Long companyId,String beneficiario);

}
