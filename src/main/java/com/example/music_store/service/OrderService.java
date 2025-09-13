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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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

        // создаём заказ (без items) и сохраняем — получаем final-ссылку
        Order order = Order.builder()
                .user(user)
                .totalPrice(cart.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .build();

        final Order savedOrder = orderRepository.save(order); // final!

        // теперь можно безопасно использовать savedOrder внутри лямбды
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .order(savedOrder)               // используем savedOrder (final)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .build();
            return orderItem;
        }).toList();

        savedOrder.setItems(orderItems);
        orderRepository.save(savedOrder); // сохраняем с заполненными items

        // очищаем корзину
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cartRepository.save(cart);

        return toDto(savedOrder);
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
