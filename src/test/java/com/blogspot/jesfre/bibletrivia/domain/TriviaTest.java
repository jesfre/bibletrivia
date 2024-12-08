package com.blogspot.jesfre.bibletrivia.domain;

import static com.blogspot.jesfre.bibletrivia.domain.TriviaQuestionTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blogspot.jesfre.bibletrivia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TriviaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trivia.class);
        Trivia trivia1 = getTriviaSample1();
        Trivia trivia2 = new Trivia();
        assertThat(trivia1).isNotEqualTo(trivia2);

        trivia2.setId(trivia1.getId());
        assertThat(trivia1).isEqualTo(trivia2);

        trivia2 = getTriviaSample2();
        assertThat(trivia1).isNotEqualTo(trivia2);
    }

    @Test
    void triviaQuestionTest() {
        Trivia trivia = getTriviaRandomSampleGenerator();
        TriviaQuestion triviaQuestionBack = getTriviaQuestionRandomSampleGenerator();

        trivia.addTriviaQuestion(triviaQuestionBack);
        assertThat(trivia.getTriviaQuestions()).containsOnly(triviaQuestionBack);

        trivia.removeTriviaQuestion(triviaQuestionBack);
        assertThat(trivia.getTriviaQuestions()).doesNotContain(triviaQuestionBack);

        trivia.triviaQuestions(new HashSet<>(Set.of(triviaQuestionBack)));
        assertThat(trivia.getTriviaQuestions()).containsOnly(triviaQuestionBack);

        trivia.setTriviaQuestions(new HashSet<>());
        assertThat(trivia.getTriviaQuestions()).doesNotContain(triviaQuestionBack);
    }
}
