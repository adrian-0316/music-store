package com.example.music_store.service;

import com.example.music_store.repository.ProductRepository;
import com.example.music_store.repository.UserRepository;

public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
}
