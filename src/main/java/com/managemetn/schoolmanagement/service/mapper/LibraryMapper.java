package com.managemetn.schoolmanagement.service.mapper;

import com.managemetn.schoolmanagement.domain.Library;
import com.managemetn.schoolmanagement.service.dto.LibraryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Library} and its DTO {@link LibraryDTO}.
 */
@Mapper(componentModel = "spring")
public interface LibraryMapper extends EntityMapper<LibraryDTO, Library> {}
