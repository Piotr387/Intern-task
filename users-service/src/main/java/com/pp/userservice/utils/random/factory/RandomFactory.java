package com.pp.userservice.utils.random.factory;

import com.pp.userservice.utils.random.singleton.RandomEnum;
import com.pp.userservice.utils.random.singleton.RandomMultithreadingLazyLoading;

public enum RandomFactory {
    ENUM, //
    MULTITHREADING_SAFE, //
    ;

    // start L1 Simple Factory
    public Random getRandomInstance(RandomFactory randomFactory) {

        return switch (randomFactory) {
            case ENUM -> RandomEnum.INSTANCE;
            case MULTITHREADING_SAFE -> RandomMultithreadingLazyLoading.getInstance();
        };
    }
}
