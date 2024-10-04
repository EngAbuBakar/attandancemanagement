package com.managemetn.schoolmanagement.service.impl;

import com.managemetn.schoolmanagement.domain.Library;
import com.managemetn.schoolmanagement.repository.LibraryRepository;
import com.managemetn.schoolmanagement.service.LibraryService;
import com.managemetn.schoolmanagement.service.dto.LibraryDTO;
import com.managemetn.schoolmanagement.service.mapper.LibraryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.managemetn.schoolmanagement.domain.Library}.
 */
@Service
@Transactional
public class LibraryServiceImpl implements LibraryService {

    private static final Logger LOG = LoggerFactory.getLogger(LibraryServiceImpl.class);

    private final LibraryRepository libraryRepository;

    private final LibraryMapper libraryMapper;

    public LibraryServiceImpl(LibraryRepository libraryRepository, LibraryMapper libraryMapper) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
    }

    @Override
    public LibraryDTO save(LibraryDTO libraryDTO) {
        LOG.debug("Request to save Library : {}", libraryDTO);
        Library library = libraryMapper.toEntity(libraryDTO);
        library = libraryRepository.save(library);
        return libraryMapper.toDto(library);
    }

    @Override
    public LibraryDTO update(LibraryDTO libraryDTO) {
        LOG.debug("Request to update Library : {}", libraryDTO);
        Library library = libraryMapper.toEntity(libraryDTO);
        library = libraryRepository.save(library);
        return libraryMapper.toDto(library);
    }

    @Override
    public Optional<LibraryDTO> partialUpdate(LibraryDTO libraryDTO) {
        LOG.debug("Request to partially update Library : {}", libraryDTO);

        return libraryRepository
            .findById(libraryDTO.getId())
            .map(existingLibrary -> {
                libraryMapper.partialUpdate(existingLibrary, libraryDTO);

                return existingLibrary;
            })
            .map(libraryRepository::save)
            .map(libraryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LibraryDTO> findOne(Long id) {
        LOG.debug("Request to get Library : {}", id);
        return libraryRepository.findById(id).map(libraryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Library : {}", id);
        libraryRepository.deleteById(id);
    }
}
