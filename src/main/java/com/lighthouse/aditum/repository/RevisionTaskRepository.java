package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.RevisionTask;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the RevisionTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RevisionTaskRepository extends JpaRepository<RevisionTask, Long> {

    List<RevisionTask> findAllByRevisionId(Long revisionId);

}