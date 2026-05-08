package com.jamie.fullstack.order_service.service;

import com.jamie.fullstack.order_service.model.Order;
import com.jamie.fullstack.order_service.model.OrderStatus;
import com.jamie.fullstack.order_service.repository.OrderRepository;
import com.jamie.fullstack.order_service.dto.OrderRequest;
import com.jamie.fullstack.order_service.event.OrderEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional
    public Order createOrder(OrderRequest request) {
        // Build and Save Entity
        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .status(OrderStatus.PENDING)
                .build();

        order = orderRepository.save(order);
        log.info("Order {} saved to DB", order.getId());
        
        // Prepare and Send Kafka Event
        OrderEvent event= new OrderEvent(order.getId(), order.getProductId(), order.getQuantity(), order.getPrice(), order.getStatus());
        kafkaTemplate.send("order-topic", event);
        log.info("Order event send to Kafka for ID: {}", order.getId());

        return order;

    }

}
