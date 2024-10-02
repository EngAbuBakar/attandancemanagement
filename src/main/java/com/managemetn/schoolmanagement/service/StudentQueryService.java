package com.managemetn.schoolmanagement.service;

import com.managemetn.schoolmanagement.domain.*; // for static metamodels
import com.managemetn.schoolmanagement.domain.Student;
import com.managemetn.schoolmanagement.repository.StudentRepository;
import com.managemetn.schoolmanagement.service.criteria.StudentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Student} entities in the database.
 * The main input is a {@link StudentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Student} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StudentQueryService extends QueryService<Student> {

    private static final Logger LOG = LoggerFactory.getLogger(StudentQueryService.class);

    private final StudentRepository studentRepository;

    public StudentQueryService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Return a {@link Page} of {@link Student} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Student> findByCriteria(StudentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Student> specification = createSpecification(criteria);
        return studentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    //    @Transactional(readOnly = true)
    //    public long countByCriteria(StudentCriteria criteria) {
    //        LOG.debug("count by criteria : {}", criteria);
    //        final Specification<Student> specification = createSpecification(criteria);
    //        return studentRepository.count(specification);
    //    }

    /**
     * Function to convert {@link StudentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Student> createSpecification(StudentCriteria criteria) {
        Specification<Student> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            //            if (criteria.getDistinct() != null) {
            //                specification = specification.and(distinct(criteria.getDistinct()));
            //            }
            //            if (criteria.getId() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getId(), Student_.id));
            //            }
            //            if (criteria.getName() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getName(), Student_.name));
            //            }
            //            if (criteria.getAge() != null) {
            //                specification = specification.and(buildRangeSpecification(criteria.getAge(), Student_.age));
            //            }
            //            if (criteria.getRollNumber() != null) {
            //                specification = specification.and(buildStringSpecification(criteria.getRollNumber(), Student_.rollNumber));
            //            }
        }
        return specification;
    }
}
