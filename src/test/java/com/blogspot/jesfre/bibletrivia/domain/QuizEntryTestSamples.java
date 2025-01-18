package com.blogspot.jesfre.bibletrivia.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class QuizEntryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static QuizEntry getQuizEntrySample1() {
        return new QuizEntry().id(1L).orderNum(1);
    }

    public static QuizEntry getQuizEntrySample2() {
        return new QuizEntry().id(2L).orderNum(2);
    }

    public static QuizEntry getQuizEntryRandomSampleGenerator() {
        return new QuizEntry().id(longCount.incrementAndGet()).orderNum(intCount.incrementAndGet());
    }
}
