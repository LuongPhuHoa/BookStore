package org.example.springcorelab;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final MessageGateway messageGateway;

    public OrderService(MessageGateway messageGateway) {
        this.messageGateway = messageGateway;
    }

    public String checkout(String orderCode) {
        return messageGateway.sendConfirmation(orderCode);
    }
}
