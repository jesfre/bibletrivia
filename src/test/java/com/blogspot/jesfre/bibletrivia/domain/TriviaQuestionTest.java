package com.blogspot.jesfre.bibletrivia.domain;

import static com.blogspot.jesfre.bibletrivia.domain.TriviaAnswerTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaQuestionTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blogspot.jesfre.bibletrivia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TriviaQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TriviaQuestion.class);
        TriviaQuestion triviaQuestion1 = getTriviaQuestionSample1();
        TriviaQuestion triviaQuestion2 = new TriviaQuestion();
        assertThat(triviaQuestion1).isNotEqualTo(triviaQuestion2);

        triviaQuestion2.setId(triviaQuestion1.getId());
        assertThat(triviaQuestion1).isEqualTo(triviaQuestion2);

        triviaQuestion2 = getTriviaQuestionSample2();
        assertThat(triviaQuestion1).isNotEqualTo(triviaQuestion2);
    }

    @Test
    void triviaAnswerTest() {
        TriviaQuestion triviaQuestion = getTriviaQuestionRandomSampleGenerator();
        TriviaAnswer triviaAnswerBack = getTriviaAnswerRandomSampleGenerator();

        triviaQuestion.addTriviaAnswer(triviaAnswerBack);
        assertThat(triviaQuestion.getTriviaAnswers()).containsOnly(triviaAnswerBack);
        assertThat(triviaAnswerBack.getTriviaQuestion()).isEqualTo(triviaQuestion);

        triviaQuestion.removeTriviaAnswer(triviaAnswerBack);
        assertThat(triviaQuestion.getTriviaAnswers()).doesNotContain(triviaAnswerBack);
        assertThat(triviaAnswerBack.getTriviaQuestion()).isNull();

        triviaQuestion.triviaAnswers(new HashSet<>(Set.of(triviaAnswerBack)));
        assertThat(triviaQuestion.getTriviaAnswers()).containsOnly(triviaAnswerBack);
        assertThat(triviaAnswerBack.getTriviaQuestion()).isEqualTo(triviaQuestion);

        triviaQuestion.setTriviaAnswers(new HashSet<>());
        assertThat(triviaQuestion.getTriviaAnswers()).doesNotContain(triviaAnswerBack);
        assertThat(triviaAnswerBack.getTriviaQuestion()).isNull();
    }

    @Test
    void triviaTest() {
        TriviaQuestion triviaQuestion = getTriviaQuestionRandomSampleGenerator();
        Trivia triviaBack = getTriviaRandomSampleGenerator();

        triviaQuestion.addTrivia(triviaBack);
        assertThat(triviaQuestion.getTrivias()).containsOnly(triviaBack);
        assertThat(triviaBack.getTriviaQuestions()).containsOnly(triviaQuestion);

        triviaQuestion.removeTrivia(triviaBack);
        assertThat(triviaQuestion.getTrivias()).doesNotContain(triviaBack);
        assertThat(triviaBack.getTriviaQuestions()).doesNotContain(triviaQuestion);

        triviaQuestion.trivias(new HashSet<>(Set.of(triviaBack)));
        assertThat(triviaQuestion.getTrivias()).containsOnly(triviaBack);
        assertThat(triviaBack.getTriviaQuestions()).containsOnly(triviaQuestion);

        triviaQuestion.setTrivias(new HashSet<>());
        assertThat(triviaQuestion.getTrivias()).doesNotContain(triviaBack);
        assertThat(triviaBack.getTriviaQuestions()).doesNotContain(triviaQuestion);
    }
}
