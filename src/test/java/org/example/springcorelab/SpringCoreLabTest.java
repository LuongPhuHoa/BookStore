package org.example.springcorelab;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class SpringCoreLabTest {

    @Test
    void containerCreatesBeanAndInjectsItsDependency() {
        try (var context = new AnnotationConfigApplicationContext(SpringCoreLabConfig.class)) {
            OrderService orderService = context.getBean(OrderService.class);

            assertEquals("Sent confirmation for ORDER-001", orderService.checkout("ORDER-001"));
        }
    }

    @Test
    void singletonAndPrototypeHaveDifferentIdentityRules() {
        try (var context = new AnnotationConfigApplicationContext(SpringCoreLabConfig.class)) {
            OrderService firstService = context.getBean(OrderService.class);
            OrderService secondService = context.getBean(OrderService.class);
            PrototypeTask firstTask = context.getBean(PrototypeTask.class);
            PrototypeTask secondTask = context.getBean(PrototypeTask.class);

            assertSame(firstService, secondService);
            assertNotSame(firstTask, secondTask);
        }
    }

    @Test
    void providerCreatesFreshPrototypeInsideSingleton() {
        try (var context = new AnnotationConfigApplicationContext(SpringCoreLabConfig.class)) {
            TaskCoordinator coordinator = context.getBean(TaskCoordinator.class);

            assertNotSame(coordinator.createTask(), coordinator.createTask());
        }
    }

    @Test
    void contextRunsLifecycleCallbacksInOrder() {
        LifecycleProbe probe;

        try (var context = new AnnotationConfigApplicationContext(SpringCoreLabConfig.class)) {
            probe = context.getBean(LifecycleProbe.class);
            assertEquals(
                    java.util.List.of("1. constructor", "2. @PostConstruct"),
                    probe.events()
            );
            probe.use();
        }

        assertEquals(
                java.util.List.of(
                        "1. constructor",
                        "2. @PostConstruct",
                        "3. bean is being used",
                        "4. @PreDestroy"
                ),
                probe.events()
        );
    }
}
