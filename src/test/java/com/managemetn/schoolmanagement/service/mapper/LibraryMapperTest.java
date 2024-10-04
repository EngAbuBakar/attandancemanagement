package com.managemetn.schoolmanagement.service.mapper;

import static com.managemetn.schoolmanagement.domain.LibraryAsserts.*;
import static com.managemetn.schoolmanagement.domain.LibraryTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LibraryMapperTest {

    private LibraryMapper libraryMapper;

    @BeforeEach
    void setUp() {
        libraryMapper = new LibraryMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getLibrarySample1();
        var actual = libraryMapper.toEntity(libraryMapper.toDto(expected));
        assertLibraryAllPropertiesEquals(expected, actual);
    }
}
