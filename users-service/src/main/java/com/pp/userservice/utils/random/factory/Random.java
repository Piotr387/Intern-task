package com.pp.userservice.utils.random.factory;

public interface Random {

    long getRandom();
    long getRandomFromClosedRange(long inclusiveStart, long inclusiveEnd);
}
