package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.KeyWords;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the KeyWords entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KeyWordsRepository extends JpaRepository<KeyWords, Long> {

}
