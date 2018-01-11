package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.DestiniesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Destinies and its DTO DestiniesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DestiniesMapper {

    DestiniesDTO destiniesToDestiniesDTO(Destinies destinies);

    List<DestiniesDTO> destiniesToDestiniesDTOs(List<Destinies> destinies);

    Destinies destiniesDTOToDestinies(DestiniesDTO destiniesDTO);

    List<Destinies> destiniesDTOsToDestinies(List<DestiniesDTO> destiniesDTOs);
}
