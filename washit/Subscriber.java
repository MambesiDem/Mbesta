package com.example.washit;

import java.util.Map;

@FunctionalInterface
public interface Subscriber {
    void readPublished(Map.Entry<String,Object>... published);
}
