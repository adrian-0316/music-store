package com.example.music_store.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class CartResponse {
    private Long id;
    private BigDecimal totalPrice;
    private List<CartItemDto> items;

    @Data
    @Builder
    public static class CartItemDto {
        private Long productId;
        private String name;
        private int quantity;
        private BigDecimal price;
    }
}
