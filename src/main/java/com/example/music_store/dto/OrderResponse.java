package com.example.music_store.dto;

import com.example.music_store.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {
    private Long id;
    private LocalDateTime createdAt;
    private Long userId;
    private double totalPrice;
    private OrderStatus status;
    private List<OrderItemDto> items;
}
