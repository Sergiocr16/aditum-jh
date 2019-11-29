

package com.lighthouse.aditum.service.mapper;

    import com.lighthouse.aditum.domain.*;
    import com.lighthouse.aditum.service.dto.HouseDTO;

    import org.mapstruct.*;
    import java.util.List;

/**
 * Mapper for the entity House and its DTO HouseDTO.
 */
@Mapper(componentModel = "spring", uses = {SubsidiaryMapper.class,SubsidiaryTypeMapper.class})
public interface HouseMapper {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "subsidiaryType.id", target = "subsidiaryTypeId")
    HouseDTO houseToHouseDTO(House house);

    List<HouseDTO> housesToHouseDTOs(List<House> houses);

    @Mapping(target = "vehicules", ignore = true)
    @Mapping(target = "visitants", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "residents", ignore = true)
    @Mapping(target = "emergencies", ignore = true)
    @Mapping(source = "companyId", target = "company")
    @Mapping(source = "subsidiaryTypeId", target = "subsidiaryType")
    @Mapping(target = "subsidiaries", ignore = true)
    House houseDTOToHouse(HouseDTO houseDTO);

    List<House> houseDTOsToHouses(List<HouseDTO> houseDTOs);

    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }


}
