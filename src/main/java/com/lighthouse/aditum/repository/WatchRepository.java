package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.Watch;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Watch entity.
 */
@SuppressWarnings("unused")
public interface WatchRepository extends JpaRepository<Watch,Long> {

}
