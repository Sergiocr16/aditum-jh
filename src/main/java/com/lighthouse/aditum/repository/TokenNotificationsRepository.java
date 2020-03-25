package com.lighthouse.aditum.repository;

import com.lighthouse.aditum.domain.TokenNotifications;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the TokenNotifications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenNotificationsRepository extends JpaRepository<TokenNotifications, Long> {

    @Query("select token_notifications from TokenNotifications token_notifications where token_notifications.user.login = ?#{principal.username}")
    List<TokenNotifications> findByUserIsCurrentUser();

    TokenNotifications findByToken(String token);

    List<TokenNotifications> findByUserId(Long userId);

}
