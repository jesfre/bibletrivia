package com.blogspot.jesfre.bibletrivia.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BibleReferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static BibleReference getBibleReferenceSample1() {
        return new BibleReference().id(1L).bibleVerse("bibleVerse1").version("version1").chapter(1).verse(1).url("url1");
    }

    public static BibleReference getBibleReferenceSample2() {
        return new BibleReference().id(2L).bibleVerse("bibleVerse2").version("version2").chapter(2).verse(2).url("url2");
    }

    public static BibleReference getBibleReferenceRandomSampleGenerator() {
        return new BibleReference()
            .id(longCount.incrementAndGet())
            .bibleVerse(UUID.randomUUID().toString())
            .version(UUID.randomUUID().toString())
            .chapter(intCount.incrementAndGet())
            .verse(intCount.incrementAndGet())
            .url(UUID.randomUUID().toString());
    }
}
