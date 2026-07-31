package org.example.springcorelab;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PrototypeTask {

    private final UUID id = UUID.randomUUID();

    public UUID id() {
        return id;
    }
}
