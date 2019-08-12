package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.InvitationSchedule;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the InvitationSchedule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InvitationScheduleRepository extends JpaRepository<InvitationSchedule, Long> {
    List<InvitationSchedule> findByVisitantInvitationId(Long invitationId);
    InvitationSchedule findOneByVisitantInvitationId(Long invitationId);

}
