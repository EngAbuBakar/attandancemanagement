package com.managemetn.schoolmanagement.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class StudentCriteriaTest {

    @Test
    void newStudentCriteriaHasAllFiltersNullTest() {
        var studentCriteria = new StudentCriteria();
        assertThat(studentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void studentCriteriaFluentMethodsCreatesFiltersTest() {
        var studentCriteria = new StudentCriteria();

        setAllFilters(studentCriteria);

        assertThat(studentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void studentCriteriaCopyCreatesNullFilterTest() {
        var studentCriteria = new StudentCriteria();
        var copy = studentCriteria.copy();

        assertThat(studentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(studentCriteria)
        );
    }

    @Test
    void studentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var studentCriteria = new StudentCriteria();
        setAllFilters(studentCriteria);

        var copy = studentCriteria.copy();

        assertThat(studentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(studentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var studentCriteria = new StudentCriteria();

        assertThat(studentCriteria).hasToString("StudentCriteria{}");
    }

    private static void setAllFilters(StudentCriteria studentCriteria) {
        studentCriteria.id();
        studentCriteria.name();
        studentCriteria.age();
        studentCriteria.rollNumber();
        studentCriteria.distinct();
    }

    private static Condition<StudentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getAge()) &&
                condition.apply(criteria.getRollNumber()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<StudentCriteria> copyFiltersAre(StudentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getAge(), copy.getAge()) &&
                condition.apply(criteria.getRollNumber(), copy.getRollNumber()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
