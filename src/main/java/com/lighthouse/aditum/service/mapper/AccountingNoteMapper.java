package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.AccountingNoteDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AccountingNote and its DTO AccountingNoteDTO.
 */
@Mapper(componentModel = "spring", uses = {HouseMapper.class, CompanyMapper.class})
public interface AccountingNoteMapper extends EntityMapper<AccountingNoteDTO, AccountingNote> {

    @Mapping(source = "house.id", target = "houseId")
    @Mapping(source = "company.id", target = "companyId")
    AccountingNoteDTO toDto(AccountingNote accountingNote);

    @Mapping(source = "houseId", target = "house")
    @Mapping(source = "companyId", target = "company")
    AccountingNote toEntity(AccountingNoteDTO accountingNoteDTO);
    default House houseFromId(Long id) {
        if (id == null) {
            return null;
        }
        House house = new House();
        house.setId(id);
        return house;
    }

    default AccountingNote fromId(Long id) {
        if (id == null) {
            return null;
        }
        AccountingNote accountingNote = new AccountingNote();
        accountingNote.setId(id);
        return accountingNote;
    }
}
