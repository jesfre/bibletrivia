package com.blogspot.jesfre.bibletrivia.domain;

import static com.blogspot.jesfre.bibletrivia.domain.QuizEntryTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.QuizTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blogspot.jesfre.bibletrivia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class QuizTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Quiz.class);
        Quiz quiz1 = getQuizSample1();
        Quiz quiz2 = new Quiz();
        assertThat(quiz1).isNotEqualTo(quiz2);

        quiz2.setId(quiz1.getId());
        assertThat(quiz1).isEqualTo(quiz2);

        quiz2 = getQuizSample2();
        assertThat(quiz1).isNotEqualTo(quiz2);
    }

    @Test
    void quizEntriesTest() {
        Quiz quiz = getQuizRandomSampleGenerator();
        QuizEntry quizEntryBack = getQuizEntryRandomSampleGenerator();

        quiz.addQuizEntries(quizEntryBack);
        assertThat(quiz.getQuizEntries()).containsOnly(quizEntryBack);
        assertThat(quizEntryBack.getQuiz()).isEqualTo(quiz);

        quiz.removeQuizEntries(quizEntryBack);
        assertThat(quiz.getQuizEntries()).doesNotContain(quizEntryBack);
        assertThat(quizEntryBack.getQuiz()).isNull();

        quiz.quizEntries(new HashSet<>(Set.of(quizEntryBack)));
        assertThat(quiz.getQuizEntries()).containsOnly(quizEntryBack);
        assertThat(quizEntryBack.getQuiz()).isEqualTo(quiz);

        quiz.setQuizEntries(new HashSet<>());
        assertThat(quiz.getQuizEntries()).doesNotContain(quizEntryBack);
        assertThat(quizEntryBack.getQuiz()).isNull();
    }
}
