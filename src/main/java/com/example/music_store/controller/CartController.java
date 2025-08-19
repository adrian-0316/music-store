package com.example.music_store.controller;

import com.example.music_store.dto.CartItemRequest;
import com.example.music_store.dto.CartResponse;
import com.example.music_store.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}/add")
    public ResponseEntity<CartResponse> addItem(@PathVariable Long userId, @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(userId, request));
    }
}
