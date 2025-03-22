package com.blogspot.jesfre.bibletrivia.domain;

import static com.blogspot.jesfre.bibletrivia.domain.QuizEntryTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.QuizTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaAnswerTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blogspot.jesfre.bibletrivia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuizEntryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuizEntry.class);
        QuizEntry quizEntry1 = getQuizEntrySample1();
        QuizEntry quizEntry2 = new QuizEntry();
        assertThat(quizEntry1).isNotEqualTo(quizEntry2);

        quizEntry2.setId(quizEntry1.getId());
        assertThat(quizEntry1).isEqualTo(quizEntry2);

        quizEntry2 = getQuizEntrySample2();
        assertThat(quizEntry1).isNotEqualTo(quizEntry2);
    }

    @Test
    void triviaQuestionTest() {
        QuizEntry quizEntry = getQuizEntryRandomSampleGenerator();
        TriviaQuestion triviaQuestionBack = getTriviaQuestionRandomSampleGenerator();

        quizEntry.setTriviaQuestion(triviaQuestionBack);
        assertThat(quizEntry.getTriviaQuestion()).isEqualTo(triviaQuestionBack);

        quizEntry.triviaQuestion(null);
        assertThat(quizEntry.getTriviaQuestion()).isNull();
    }

    @Test
    void triviaAnswersTest() {
        QuizEntry quizEntry = getQuizEntryRandomSampleGenerator();
        TriviaAnswer triviaAnswerBack = getTriviaAnswerRandomSampleGenerator();

        quizEntry.addSelectedAnswers(triviaAnswerBack);
        assertThat(quizEntry.getSelectedAnswers()).containsOnly(triviaAnswerBack);

        quizEntry.removeSelectedAnswers(triviaAnswerBack);
        assertThat(quizEntry.getSelectedAnswers()).doesNotContain(triviaAnswerBack);

        quizEntry.selectedAnswers(new HashSet<>(Set.of(triviaAnswerBack)));
        assertThat(quizEntry.getSelectedAnswers()).containsOnly(triviaAnswerBack);

        quizEntry.setSelectedAnswers(new HashSet<>());
        assertThat(quizEntry.getSelectedAnswers()).doesNotContain(triviaAnswerBack);
    }

    @Test
    void quizTest() {
        QuizEntry quizEntry = getQuizEntryRandomSampleGenerator();
        Quiz quizBack = getQuizRandomSampleGenerator();

        quizEntry.setQuiz(quizBack);
        assertThat(quizEntry.getQuiz()).isEqualTo(quizBack);

        quizEntry.quiz(null);
        assertThat(quizEntry.getQuiz()).isNull();
    }
}
