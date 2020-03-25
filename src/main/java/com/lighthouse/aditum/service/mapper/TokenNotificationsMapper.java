package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.TokenNotificationsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity TokenNotifications and its DTO TokenNotificationsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface TokenNotificationsMapper extends EntityMapper<TokenNotificationsDTO, TokenNotifications> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    TokenNotificationsDTO toDto(TokenNotifications tokenNotifications); 

    @Mapping(source = "userId", target = "user")
    TokenNotifications toEntity(TokenNotificationsDTO tokenNotificationsDTO);

    default TokenNotifications fromId(Long id) {
        if (id == null) {
            return null;
        }
        TokenNotifications tokenNotifications = new TokenNotifications();
        tokenNotifications.setId(id);
        return tokenNotifications;
    }
}
