package com.example.music_store.ui;

import com.example.music_store.dto.CartItemRequest;
import com.example.music_store.dto.CartResponse;
import com.example.music_store.service.CartService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

@Route("cart")
@RequiredArgsConstructor
public class CartView extends VerticalLayout {

    private final CartService cartService;

    private final Grid<CartResponse.CartItemDto> grid = new Grid<>(CartResponse.CartItemDto.class);
    private final H2 totalPrice = new H2("Total: 0");

    private final Long demoUserId = 1L; //Пока заглушка

    public CartView(CartService cartService) {
        this.cartService = cartService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("🛒 Cart"));

        grid.setColumns("name", "quantity", "price");
        add(grid, totalPrice);

        Button refreshBtn = new Button("Обновить", e -> loadCart());
        Button clearBtn = new Button("Очистить корзину", e -> clearCart());
        add(refreshBtn, clearBtn);

        loadCart();
    }

    private void loadCart() {
        CartResponse response = cartService.getCart(demoUserId);
        grid.setItems(response.getItems());
        totalPrice.setText("Total: " + response.getTotalPrice());
    }

    private void clearCart() {
        CartResponse response = cartService.getCart(demoUserId);
        response.getItems().forEach(item ->
                cartService.removeItem(demoUserId, item.getProductId())
        );
        loadCart();
    }
}
