package com.example.music_store.service;

import com.example.music_store.dto.CartItemRequest;
import com.example.music_store.dto.CartResponse;
import com.example.music_store.entity.Cart;
import com.example.music_store.entity.CartItem;
import com.example.music_store.entity.Product;
import com.example.music_store.entity.User;
import com.example.music_store.repository.CartItemRepository;
import com.example.music_store.repository.CartRepository;
import com.example.music_store.repository.ProductRepository;
import com.example.music_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartResponse addItem(Long userId, CartItemRequest request) {
        User user = userRepository.findById(userId).
                orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setTotalPrice(BigDecimal.ZERO);
            return cartRepository.save(newCart);
        });
        Product product = productRepository.findById(request.getProductId()).
                orElseThrow(() -> new RuntimeException("Product not found"));
        CartItem item = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(request.getQuantity())
                .price(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())))
                .build();
        cart.getItems().add(item);
        cart.setTotalPrice(cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        cartItemRepository.save(item);
        cartRepository.save(cart);

        return toDto(cart);
    }
    private CartResponse toDto(Cart cart) {
        return CartResponse.builder()
                .id(cart.getId())
                .totalPrice(cart.getTotalPrice())
                .items(cart.getItems().stream().map(item ->
                        CartResponse.CartItemDto.builder()
                                .productId(item.getProduct().getId())
                                .name(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build()
                ).toList())
                .build();
    }
}
