package com.blogspot.jesfre.bibletrivia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TriviaAnswerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TriviaAnswer getTriviaAnswerSample1() {
        return new TriviaAnswer().id(1L).answerId(1L).answer("answer1").explanation("explanation1").picture("picture1");
    }

    public static TriviaAnswer getTriviaAnswerSample2() {
        return new TriviaAnswer().id(2L).answerId(2L).answer("answer2").explanation("explanation2").picture("picture2");
    }

    public static TriviaAnswer getTriviaAnswerRandomSampleGenerator() {
        return new TriviaAnswer()
            .id(longCount.incrementAndGet())
            .answerId(longCount.incrementAndGet())
            .answer(UUID.randomUUID().toString())
            .explanation(UUID.randomUUID().toString())
            .picture(UUID.randomUUID().toString());
    }
}
