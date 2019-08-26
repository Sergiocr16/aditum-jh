package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Note entity.
 */
@SuppressWarnings("unused")
public interface NoteRepository extends JpaRepository<Note,Long> {
    Page<Note> findByCompanyIdAndDeletedAndStatus(Pageable pageable, Long companyId, Integer deleted, Integer status);
    Page<Note> findByCompanyIdAndDeletedAndStatusGreaterThan(Pageable pageable, Long companyId, Integer deleted, Integer status);
    Page<Note> findByHouseIdAndDeletedAndStatus(Pageable pageable, Long companyId, Integer deleted, Integer status);
    Page<Note> findByHouseIdAndDeletedAndStatusGreaterThan(Pageable pageable, Long companyId, Integer deleted, Integer status);
}
