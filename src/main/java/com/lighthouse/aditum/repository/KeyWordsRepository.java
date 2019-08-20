package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.KeyWords;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the KeyWords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyWordsRepository extends JpaRepository<KeyWords, Long> {
    Page<KeyWords> findByDeleted(Pageable pageable, int deleted);
}
