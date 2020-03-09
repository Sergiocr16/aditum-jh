package com.lighthouse.aditum.service.mapper;

import com.lighthouse.aditum.domain.*;
import com.lighthouse.aditum.service.dto.TasksDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Tasks and its DTO TasksDTO.
 */
@Mapper(componentModel = "spring", uses = {CompanyMapper.class})
public interface TasksMapper extends EntityMapper<TasksDTO, Tasks> {

    @Mapping(source = "company.id", target = "companyId")
    TasksDTO toDto(Tasks tasks);

    @Mapping(source = "companyId", target = "company")
    Tasks toEntity(TasksDTO tasksDTO);
    default Company companyFromId(Long id) {
        if (id == null) {
            return null;
        }
        Company company = new Company();
        company.setId(id);
        return company;
    }
    default Tasks fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tasks tasks = new Tasks();
        tasks.setId(id);
        return tasks;
    }
}
