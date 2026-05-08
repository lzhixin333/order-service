package com.jamie.fullstack.order_service.service;

import com.jamie.fullstack.order_service.event.OrderEvent;
import com.jamie.fullstack.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentResultConsumer {
    private final OrderRepository orderRepository;

    @KafkaListener(topics = "payment-result-topic", groupId = "order-group")
    public void consumePaymentResult(OrderEvent event) {
        log.info("Received payment result: Order {} is now {}", event.getOrderId(), event.getStatus());

        orderRepository.findById(event.getOrderId()).ifPresent(order -> {
            order.setStatus(event.getStatus()); // This updates the Enum in Postgres
            orderRepository.save(order);
            log.info("Database updated for Order {}", order.getId());
        });
    }
}
