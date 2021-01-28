package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AccountingNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the AccountingNote entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AccountingNoteRepository extends JpaRepository<AccountingNote, Long> {

    Page<AccountingNote> findAllByHouseIdAndDeleted(Pageable page,Long houseId,int deleted);


}
