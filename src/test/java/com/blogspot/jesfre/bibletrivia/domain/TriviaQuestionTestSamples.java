package com.blogspot.jesfre.bibletrivia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TriviaQuestionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TriviaQuestion getTriviaQuestionSample1() {
        return new TriviaQuestion().id(1L).questionId(1L).question("question1").value(1).picture("picture1");
    }

    public static TriviaQuestion getTriviaQuestionSample2() {
        return new TriviaQuestion().id(2L).questionId(2L).question("question2").value(2).picture("picture2");
    }

    public static TriviaQuestion getTriviaQuestionRandomSampleGenerator() {
        return new TriviaQuestion()
            .id(longCount.incrementAndGet())
            .questionId(longCount.incrementAndGet())
            .question(UUID.randomUUID().toString())
            .value(intCount.incrementAndGet())
            .picture(UUID.randomUUID().toString());
    }
}
