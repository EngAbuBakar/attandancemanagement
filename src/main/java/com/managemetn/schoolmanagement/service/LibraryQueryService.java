package com.managemetn.schoolmanagement.service;

import com.managemetn.schoolmanagement.domain.*; // for static metamodels
import com.managemetn.schoolmanagement.domain.Library;
import com.managemetn.schoolmanagement.repository.LibraryRepository;
import com.managemetn.schoolmanagement.service.criteria.LibraryCriteria;
import com.managemetn.schoolmanagement.service.dto.LibraryDTO;
import com.managemetn.schoolmanagement.service.mapper.LibraryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Library} entities in the database.
 * The main input is a {@link LibraryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LibraryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LibraryQueryService extends QueryService<Library> {

    private static final Logger LOG = LoggerFactory.getLogger(LibraryQueryService.class);

    private final LibraryRepository libraryRepository;

    private final LibraryMapper libraryMapper;

    public LibraryQueryService(LibraryRepository libraryRepository, LibraryMapper libraryMapper) {
        this.libraryRepository = libraryRepository;
        this.libraryMapper = libraryMapper;
    }

    /**
     * Return a {@link Page} of {@link LibraryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LibraryDTO> findByCriteria(LibraryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Library> specification = createSpecification(criteria);
        return libraryRepository.findAll(specification, page).map(libraryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LibraryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Library> specification = createSpecification(criteria);
        return libraryRepository.count(specification);
    }

    /**
     * Function to convert {@link LibraryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Library> createSpecification(LibraryCriteria criteria) {
        Specification<Library> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            //            if (criteria.getId() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getId(), Library_.id));
            //            }
            //            if (criteria.getName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getName(), Library_.name));
            //            }
            //            if (criteria.getCode() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getCode(), Library_.code));
            //            }
            //            if (criteria.getBlock() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getBlock(), Library_.block));
            //            }
            //            if (criteria.getIsVisible() != null) {
            //                specification = specification.and(buildSpecification(criteria.getIsVisible(), Library_.isVisible));
            //            }
            //            if (criteria.getIsDeleted() != null) {
            //                specification = specification.and(buildSpecification(criteria.getIsDeleted(), Library_.isDeleted));
            //            }
            //            if (criteria.getCreatedBy() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Library_.createdBy));
            //            }
            //            if (criteria.getCreatedDate() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Library_.createdDate));
            //            }
            //            if (criteria.getLastModifiedBy() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Library_.lastModifiedBy));
            //            }
            //            if (criteria.getLastModifiedDate() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Library_.lastModifiedDate));
            //            }
        }
        return specification;
    }
}
