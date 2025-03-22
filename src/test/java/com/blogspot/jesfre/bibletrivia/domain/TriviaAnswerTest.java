package com.blogspot.jesfre.bibletrivia.domain;

import static com.blogspot.jesfre.bibletrivia.domain.BibleReferenceTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.QuizEntryTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaAnswerTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaQuestionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blogspot.jesfre.bibletrivia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TriviaAnswerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TriviaAnswer.class);
        TriviaAnswer triviaAnswer1 = getTriviaAnswerSample1();
        TriviaAnswer triviaAnswer2 = new TriviaAnswer();
        assertThat(triviaAnswer1).isNotEqualTo(triviaAnswer2);

        triviaAnswer2.setId(triviaAnswer1.getId());
        assertThat(triviaAnswer1).isEqualTo(triviaAnswer2);

        triviaAnswer2 = getTriviaAnswerSample2();
        assertThat(triviaAnswer1).isNotEqualTo(triviaAnswer2);
    }

    @Test
    void bibleReferenceTest() {
        TriviaAnswer triviaAnswer = getTriviaAnswerRandomSampleGenerator();
        BibleReference bibleReferenceBack = getBibleReferenceRandomSampleGenerator();

        triviaAnswer.addBibleReference(bibleReferenceBack);
        assertThat(triviaAnswer.getBibleReferences()).containsOnly(bibleReferenceBack);

        triviaAnswer.removeBibleReference(bibleReferenceBack);
        assertThat(triviaAnswer.getBibleReferences()).doesNotContain(bibleReferenceBack);

        triviaAnswer.bibleReferences(new HashSet<>(Set.of(bibleReferenceBack)));
        assertThat(triviaAnswer.getBibleReferences()).containsOnly(bibleReferenceBack);

        triviaAnswer.setBibleReferences(new HashSet<>());
        assertThat(triviaAnswer.getBibleReferences()).doesNotContain(bibleReferenceBack);
    }

    @Test
    void triviaQuestionTest() {
        TriviaAnswer triviaAnswer = getTriviaAnswerRandomSampleGenerator();
        TriviaQuestion triviaQuestionBack = getTriviaQuestionRandomSampleGenerator();

        triviaAnswer.setTriviaQuestion(triviaQuestionBack);
        assertThat(triviaAnswer.getTriviaQuestion()).isEqualTo(triviaQuestionBack);

        triviaAnswer.triviaQuestion(null);
        assertThat(triviaAnswer.getTriviaQuestion()).isNull();
    }

    @Test
    void quizEntriesTest() {
        TriviaAnswer triviaAnswer = getTriviaAnswerRandomSampleGenerator();
        QuizEntry quizEntryBack = getQuizEntryRandomSampleGenerator();

        triviaAnswer.addQuizEntries(quizEntryBack);
        assertThat(triviaAnswer.getQuizEntries()).containsOnly(quizEntryBack);
        assertThat(quizEntryBack.getSelectedAnswers()).containsOnly(triviaAnswer);

        triviaAnswer.removeQuizEntries(quizEntryBack);
        assertThat(triviaAnswer.getQuizEntries()).doesNotContain(quizEntryBack);
        assertThat(quizEntryBack.getSelectedAnswers()).doesNotContain(triviaAnswer);

        triviaAnswer.quizEntries(new HashSet<>(Set.of(quizEntryBack)));
        assertThat(triviaAnswer.getQuizEntries()).containsOnly(quizEntryBack);
        assertThat(quizEntryBack.getSelectedAnswers()).containsOnly(triviaAnswer);

        triviaAnswer.setQuizEntries(new HashSet<>());
        assertThat(triviaAnswer.getQuizEntries()).doesNotContain(quizEntryBack);
        assertThat(quizEntryBack.getSelectedAnswers()).doesNotContain(triviaAnswer);
    }
}
