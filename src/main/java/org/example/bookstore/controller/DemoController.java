package org.example.bookstore.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@RestController
@RequestMapping("/api/demo")
public class DemoController {

    @GetMapping("/virtual-thread")
    public List<String> virtualThreadDemo() throws Exception {
        List<Callable<String>> externalServiceCalls = List.of(
                () -> simulateExternalServiceCall("inventory-service"),
                () -> simulateExternalServiceCall("pricing-service"),
                () -> simulateExternalServiceCall("recommendation-service"),
                () -> simulateExternalServiceCall("review-service"),
                () -> simulateExternalServiceCall("shipping-service")
        );

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<String>> futures = new ArrayList<>();
            for (Callable<String> task : externalServiceCalls) {
                futures.add(executor.submit(task));
            }

            List<String> results = new ArrayList<>();
            for (Future<String> future : futures) {
                results.add(future.get());
            }
            return results;
        }
    }

    private String simulateExternalServiceCall(String serviceName) throws InterruptedException {
        Thread.sleep(Duration.ofMillis(250));
        return serviceName + " responded on " + Thread.currentThread();
    }
}
