package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.NotificationSended;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the NotificationSended entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationSendedRepository extends JpaRepository<NotificationSended, Long> {

}
