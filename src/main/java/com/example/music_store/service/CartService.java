package com.example.music_store.service;

import com.example.music_store.repository.CartItemRepository;
import com.example.music_store.repository.CartRepository;
import com.example.music_store.repository.ProductRepository;
import com.example.music_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartRes
}
