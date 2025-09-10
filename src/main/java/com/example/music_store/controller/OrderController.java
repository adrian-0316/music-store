package com.example.music_store.controller;

import com.example.music_store.dto.OrderResponse;
import com.example.music_store.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/{userId}/place")
    public OrderResponse placeOrder(@PathVariable Long userId) {
        return orderService.placeOrder(userId);
    }
}
