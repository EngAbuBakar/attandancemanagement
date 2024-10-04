package com.managemetn.schoolmanagement.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LibraryCriteriaTest {

    @Test
    void newLibraryCriteriaHasAllFiltersNullTest() {
        var libraryCriteria = new LibraryCriteria();
        assertThat(libraryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void libraryCriteriaFluentMethodsCreatesFiltersTest() {
        var libraryCriteria = new LibraryCriteria();

        setAllFilters(libraryCriteria);

        assertThat(libraryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void libraryCriteriaCopyCreatesNullFilterTest() {
        var libraryCriteria = new LibraryCriteria();
        var copy = libraryCriteria.copy();

        assertThat(libraryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(libraryCriteria)
        );
    }

    @Test
    void libraryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var libraryCriteria = new LibraryCriteria();
        setAllFilters(libraryCriteria);

        var copy = libraryCriteria.copy();

        assertThat(libraryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(libraryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var libraryCriteria = new LibraryCriteria();

        assertThat(libraryCriteria).hasToString("LibraryCriteria{}");
    }

    private static void setAllFilters(LibraryCriteria libraryCriteria) {
        libraryCriteria.id();
        libraryCriteria.name();
        libraryCriteria.code();
        libraryCriteria.block();
        libraryCriteria.isVisible();
        libraryCriteria.isDeleted();
        libraryCriteria.createdBy();
        libraryCriteria.createdDate();
        libraryCriteria.lastModifiedBy();
        libraryCriteria.lastModifiedDate();
        libraryCriteria.distinct();
    }

    private static Condition<LibraryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getBlock()) &&
                condition.apply(criteria.getIsVisible()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LibraryCriteria> copyFiltersAre(LibraryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getBlock(), copy.getBlock()) &&
                condition.apply(criteria.getIsVisible(), copy.getIsVisible()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
