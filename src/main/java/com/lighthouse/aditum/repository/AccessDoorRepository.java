package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.AccessDoor;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the AccessDoor entity.
 */
@SuppressWarnings("unused")
public interface AccessDoorRepository extends JpaRepository<AccessDoor,Long> {

}
