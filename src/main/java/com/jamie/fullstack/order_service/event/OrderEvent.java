package com.jamie.fullstack.order_service.event;

import com.jamie.fullstack.order_service.model.OrderStatus;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private Long orderId;
    private String productId;
    private Integer quantity;
    private BigDecimal price;
    private OrderStatus status;
}
