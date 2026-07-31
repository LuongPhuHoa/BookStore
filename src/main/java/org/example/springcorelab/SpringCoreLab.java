package org.example.springcorelab;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public final class SpringCoreLab {

    private SpringCoreLab() {
    }

    public static void main(String[] args) {
        LifecycleProbe lifecycleProbe;

        try (var context = new AnnotationConfigApplicationContext(SpringCoreLabConfig.class)) {
            var firstService = context.getBean(OrderService.class);
            var secondService = context.getBean(OrderService.class);
            System.out.println("IoC + DI: " + firstService.checkout("ORDER-001"));
            System.out.println("Singleton returns same object: " + (firstService == secondService));

            var coordinator = context.getBean(TaskCoordinator.class);
            var firstTask = coordinator.createTask();
            var secondTask = coordinator.createTask();
            System.out.println("Prototype task 1: " + firstTask.id());
            System.out.println("Prototype task 2: " + secondTask.id());
            System.out.println("Prototype returns different objects: " + (firstTask != secondTask));

            lifecycleProbe = context.getBean(LifecycleProbe.class);
            lifecycleProbe.use();
            System.out.println("Before closing context: " + lifecycleProbe.events());
        }

        System.out.println("After closing context: " + lifecycleProbe.events());
    }
}
