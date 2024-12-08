package com.blogspot.jesfre.bibletrivia.domain;

import static com.blogspot.jesfre.bibletrivia.domain.BibleReferenceTestSamples.*;
import static com.blogspot.jesfre.bibletrivia.domain.TriviaAnswerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blogspot.jesfre.bibletrivia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BibleReferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BibleReference.class);
        BibleReference bibleReference1 = getBibleReferenceSample1();
        BibleReference bibleReference2 = new BibleReference();
        assertThat(bibleReference1).isNotEqualTo(bibleReference2);

        bibleReference2.setId(bibleReference1.getId());
        assertThat(bibleReference1).isEqualTo(bibleReference2);

        bibleReference2 = getBibleReferenceSample2();
        assertThat(bibleReference1).isNotEqualTo(bibleReference2);
    }

    @Test
    void triviaAnswerTest() {
        BibleReference bibleReference = getBibleReferenceRandomSampleGenerator();
        TriviaAnswer triviaAnswerBack = getTriviaAnswerRandomSampleGenerator();

        bibleReference.addTriviaAnswer(triviaAnswerBack);
        assertThat(bibleReference.getTriviaAnswers()).containsOnly(triviaAnswerBack);
        assertThat(triviaAnswerBack.getBibleReferences()).containsOnly(bibleReference);

        bibleReference.removeTriviaAnswer(triviaAnswerBack);
        assertThat(bibleReference.getTriviaAnswers()).doesNotContain(triviaAnswerBack);
        assertThat(triviaAnswerBack.getBibleReferences()).doesNotContain(bibleReference);

        bibleReference.triviaAnswers(new HashSet<>(Set.of(triviaAnswerBack)));
        assertThat(bibleReference.getTriviaAnswers()).containsOnly(triviaAnswerBack);
        assertThat(triviaAnswerBack.getBibleReferences()).containsOnly(bibleReference);

        bibleReference.setTriviaAnswers(new HashSet<>());
        assertThat(bibleReference.getTriviaAnswers()).doesNotContain(triviaAnswerBack);
        assertThat(triviaAnswerBack.getBibleReferences()).doesNotContain(bibleReference);
    }
}
