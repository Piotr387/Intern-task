package com.pp.userservice.lecture.DesignPatterns.Iterator;

public interface Aggregate<T> {
    Iterator<T> createIterator();
}
