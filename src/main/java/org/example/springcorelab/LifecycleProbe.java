package org.example.springcorelab;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LifecycleProbe {

    private final List<String> events = new ArrayList<>();

    public LifecycleProbe() {
        events.add("1. constructor");
    }

    @PostConstruct
    void initialize() {
        events.add("2. @PostConstruct");
    }

    public void use() {
        events.add("3. bean is being used");
    }

    @PreDestroy
    void destroy() {
        events.add("4. @PreDestroy");
    }

    public List<String> events() {
        return List.copyOf(events);
    }
}
