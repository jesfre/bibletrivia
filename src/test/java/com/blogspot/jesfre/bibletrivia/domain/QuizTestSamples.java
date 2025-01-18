package com.blogspot.jesfre.bibletrivia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Quiz getQuizSample1() {
        return new Quiz().id(1L).quizTaker("quizTaker1").totalQuestions(1).correctQuestions(1);
    }

    public static Quiz getQuizSample2() {
        return new Quiz().id(2L).quizTaker("quizTaker2").totalQuestions(2).correctQuestions(2);
    }

    public static Quiz getQuizRandomSampleGenerator() {
        return new Quiz()
            .id(longCount.incrementAndGet())
            .quizTaker(UUID.randomUUID().toString())
            .totalQuestions(intCount.incrementAndGet())
            .correctQuestions(intCount.incrementAndGet());
    }
}
