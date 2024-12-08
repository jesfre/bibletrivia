package com.blogspot.jesfre.bibletrivia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TriviaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Trivia getTriviaSample1() {
        return new Trivia().id(1L).name("name1");
    }

    public static Trivia getTriviaSample2() {
        return new Trivia().id(2L).name("name2");
    }

    public static Trivia getTriviaRandomSampleGenerator() {
        return new Trivia().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
