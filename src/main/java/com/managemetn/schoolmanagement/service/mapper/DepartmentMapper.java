package com.managemetn.schoolmanagement.service.mapper;

import com.managemetn.schoolmanagement.domain.Department;
import com.managemetn.schoolmanagement.service.dto.DepartmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Department} and its DTO {@link DepartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper extends EntityMapper<DepartmentDTO, Department> {}
