package com.example.music_store.service;

import com.example.music_store.dto.OrderResponse;
import com.example.music_store.entity.Cart;
import com.example.music_store.entity.CartItem;
import com.example.music_store.entity.Order;
import com.example.music_store.entity.OrderItem;
import com.example.music_store.entity.User;
import com.example.music_store.repository.CartRepository;
import com.example.music_store.repository.OrderRepository;
import com.example.music_store.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public OrderResponse placeOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Создаем заказ
        Order order = new Order();
        order.setUser(user);
        order.setTotalPrice(cart.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());

        // Переносим позиции из корзины в заказ
        order.setItems(
                cart.getItems().stream().map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(cartItem.getProduct());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    return orderItem;
                }).collect(Collectors.toList())
        );

        order = orderRepository.save(order);

        // очищаем корзину
        cart.getItems().clear();
        cart.setTotalPrice(null);
        cartRepository.save(cart);

        return toDto(order);
    }

    private OrderResponse toDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .totalPrice(order.getTotalPrice())
                .items(order.getItems().stream()
                        .map(item -> OrderResponse.OrderItemDto.builder()
                                .productId(item.getProduct().getId())
                                .name(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build()
                        ).toList())
                .build();
    }
}
