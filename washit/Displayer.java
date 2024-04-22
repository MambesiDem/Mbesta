package com.example.washit;

@FunctionalInterface
public interface Displayer<T> {
    void display(T value);
}
