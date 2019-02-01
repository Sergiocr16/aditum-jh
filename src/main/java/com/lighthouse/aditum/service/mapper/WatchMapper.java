package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.WatchDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Watch and its DTO WatchDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface WatchMapper {

    @Mapping(source = "company.id", target = "companyId")
    WatchDTO watchToWatchDTO(Watch watch);

    List<WatchDTO> watchesToWatchDTOs(List<Watch> watches);

//    @Mapping(target = "accessDoors", ignore = true)
    @Mapping(source = "companyId", target = "company")
    Watch watchDTOToWatch(WatchDTO watchDTO);

    List<Watch> watchDTOsToWatches(List<WatchDTO> watchDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
}
