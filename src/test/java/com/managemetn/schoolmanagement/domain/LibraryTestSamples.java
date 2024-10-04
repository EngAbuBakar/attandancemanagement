package com.managemetn.schoolmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LibraryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Library getLibrarySample1() {
        return new Library().id(1L).name("name1").code(1L).block("block1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static Library getLibrarySample2() {
        return new Library().id(2L).name("name2").code(2L).block("block2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static Library getLibraryRandomSampleGenerator() {
        return new Library()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .code(longCount.incrementAndGet())
            .block(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
