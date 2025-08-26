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
        recalcCart(cart);

        cartItemRepository.save(item);
        cartRepository.save(cart);

        return toDto(cart);
    }

    // 🔹 Новый метод — получить корзину пользователя
    public CartResponse getCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });
        return toDto(cart);
    }

    // 🔹 Новый метод — удалить товар из корзины
    public CartResponse removeItem(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(ci -> ci.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found"));

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        recalcCart(cart);
        cartRepository.save(cart);

        return toDto(cart);
    }

    // 🔹 Пересчет суммы корзины
    private void recalcCart(Cart cart) {
        cart.setTotalPrice(cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
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
