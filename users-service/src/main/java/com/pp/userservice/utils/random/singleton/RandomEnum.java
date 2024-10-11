package com.pp.userservice.utils.random.singleton;

import com.pp.userservice.utils.random.factory.Random;
import java.util.concurrent.ThreadLocalRandom;

// start L1 Singleton - first example implementation
public enum RandomEnum implements Random {
    INSTANCE;
    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();

    @Override
    public long getRandom() {
        return threadLocalRandom.nextLong();
    }

    @Override
    public long getRandomFromClosedRange(long inclusiveStart, long inclusiveEnd) {
        return threadLocalRandom.nextLong(inclusiveStart, inclusiveEnd + 1);
    }
}