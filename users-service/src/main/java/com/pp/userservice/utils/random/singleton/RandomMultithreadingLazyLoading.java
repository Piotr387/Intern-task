package com.pp.userservice.utils.random.singleton;

import com.pp.userservice.utils.random.factory.Random;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// start L1 Singleton - second example implementation
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RandomMultithreadingLazyLoading implements Random {

    private static volatile RandomMultithreadingLazyLoading instance;
    private final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();


    public static RandomMultithreadingLazyLoading getInstance() {
        RandomMultithreadingLazyLoading result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RandomMultithreadingLazyLoading.class) {
            if (instance == null) {
                instance = new RandomMultithreadingLazyLoading();
            }
            return instance;
        }
    }

    @Override
    public long getRandom() {
        return threadLocalRandom.nextLong();
    }

    @Override
    public long getRandomFromClosedRange(long inclusiveStart, long inclusiveEnd) {
        return threadLocalRandom.nextLong(inclusiveStart, inclusiveEnd + 1);
    }
}
