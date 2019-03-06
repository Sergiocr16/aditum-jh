package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.DocumentFile;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the DocumentFile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentFileRepository extends JpaRepository<DocumentFile, Long> {

}
