package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.DocumentFileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DocumentFile and its DTO DocumentFileDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface DocumentFileMapper extends EntityMapper<DocumentFileDTO, DocumentFile> {

    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "company.name", target = "companyName")
    DocumentFileDTO toDto(DocumentFile documentFile); 

    @Mapping(source = "companyId", target = "company")
    DocumentFile toEntity(DocumentFileDTO documentFileDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default DocumentFile fromId(Long id) {
        if (id == null) {
            return null;
        }
        DocumentFile documentFile = new DocumentFile();
        documentFile.setId(id);
        return documentFile;
    }
}
