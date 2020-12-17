package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.NotificationSendedDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity NotificationSended and its DTO NotificationSendedDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface NotificationSendedMapper extends EntityMapper<NotificationSendedDTO, NotificationSended> {

    @Mapping(source = "company.id", target = "companyId")
    NotificationSendedDTO toDto(NotificationSended notificationSended);

    @Mapping(source = "companyId", target = "company")
    NotificationSended toEntity(NotificationSendedDTO notificationSendedDTO);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }

    default NotificationSended fromId(Long id) {
        if (id == null) {
            return null;
        }
        NotificationSended notificationSended = new NotificationSended();
        notificationSended.setId(id);
        return notificationSended;
    }
}
