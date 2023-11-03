package com.example.project;

import java.util.Map;

@FunctionalInterface
public interface Subscriber {
    void readPublished(Map.Entry<String,Object>... published);
}
