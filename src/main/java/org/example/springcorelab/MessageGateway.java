package org.example.springcorelab;

@FunctionalInterface
public interface MessageGateway {

    String sendConfirmation(String orderCode);
}
