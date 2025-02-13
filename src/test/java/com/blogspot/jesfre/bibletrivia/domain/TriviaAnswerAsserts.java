package com.blogspot.jesfre.bibletrivia.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TriviaAnswerAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaAnswerAllPropertiesEquals(TriviaAnswer expected, TriviaAnswer actual) {
        assertTriviaAnswerAutoGeneratedPropertiesEquals(expected, actual);
        assertTriviaAnswerAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaAnswerAllUpdatablePropertiesEquals(TriviaAnswer expected, TriviaAnswer actual) {
        assertTriviaAnswerUpdatableFieldsEquals(expected, actual);
        assertTriviaAnswerUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaAnswerAutoGeneratedPropertiesEquals(TriviaAnswer expected, TriviaAnswer actual) {
        assertThat(expected)
            .as("Verify TriviaAnswer auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaAnswerUpdatableFieldsEquals(TriviaAnswer expected, TriviaAnswer actual) {
        assertThat(expected)
            .as("Verify TriviaAnswer relevant properties")
            .satisfies(e -> assertThat(e.getAnswerId()).as("check answerId").isEqualTo(actual.getAnswerId()))
            .satisfies(e -> assertThat(e.getAnswer()).as("check answer").isEqualTo(actual.getAnswer()))
            .satisfies(e -> assertThat(e.getExplanation()).as("check explanation").isEqualTo(actual.getExplanation()))
            .satisfies(e -> assertThat(e.getCorrect()).as("check correct").isEqualTo(actual.getCorrect()))
            .satisfies(e -> assertThat(e.getPicture()).as("check picture").isEqualTo(actual.getPicture()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTriviaAnswerUpdatableRelationshipsEquals(TriviaAnswer expected, TriviaAnswer actual) {
        assertThat(expected)
            .as("Verify TriviaAnswer relationships")
            .satisfies(e -> assertThat(e.getBibleReferences()).as("check bibleReferences").isEqualTo(actual.getBibleReferences()))
            .satisfies(e -> assertThat(e.getTriviaQuestion()).as("check triviaQuestion").isEqualTo(actual.getTriviaQuestion()))
            .satisfies(e -> assertThat(e.getQuizEntries()).as("check quizEntries").isEqualTo(actual.getQuizEntries()));
    }
}
