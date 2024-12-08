package com.blogspot.jesfre.bibletrivia.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TriviaQuestionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaQuestionAllPropertiesEquals(TriviaQuestion expected, TriviaQuestion actual) {
        assertTriviaQuestionAutoGeneratedPropertiesEquals(expected, actual);
        assertTriviaQuestionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaQuestionAllUpdatablePropertiesEquals(TriviaQuestion expected, TriviaQuestion actual) {
        assertTriviaQuestionUpdatableFieldsEquals(expected, actual);
        assertTriviaQuestionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaQuestionAutoGeneratedPropertiesEquals(TriviaQuestion expected, TriviaQuestion actual) {
        assertThat(expected)
            .as("Verify TriviaQuestion auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaQuestionUpdatableFieldsEquals(TriviaQuestion expected, TriviaQuestion actual) {
        assertThat(expected)
            .as("Verify TriviaQuestion relevant properties")
            .satisfies(e -> assertThat(e.getQuestionId()).as("check questionId").isEqualTo(actual.getQuestionId()))
            .satisfies(e -> assertThat(e.getQuestionType()).as("check questionType").isEqualTo(actual.getQuestionType()))
            .satisfies(e -> assertThat(e.getQuestion()).as("check question").isEqualTo(actual.getQuestion()))
            .satisfies(e -> assertThat(e.getAnswerType()).as("check answerType").isEqualTo(actual.getAnswerType()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").isEqualTo(actual.getValue()))
            .satisfies(e -> assertThat(e.getPicture()).as("check picture").isEqualTo(actual.getPicture()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaQuestionUpdatableRelationshipsEquals(TriviaQuestion expected, TriviaQuestion actual) {
        assertThat(expected)
            .as("Verify TriviaQuestion relationships")
            .satisfies(e -> assertThat(e.getTrivias()).as("check trivias").isEqualTo(actual.getTrivias()));
    }
}
