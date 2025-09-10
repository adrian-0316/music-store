package com.example.music_store.ui;

import com.example.music_store.dto.CartResponse;
import com.example.music_store.service.CartService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

@Route("cart")
@RequiredArgsConstructor
public class CartView extends VerticalLayout {

    private final CartService cartService;

    private final Grid<CartResponse.CartItemDto> grid = new Grid<>(CartResponse.CartItemDto.class);
    private final H2 totalPrice = new H2("Total: 0");

    private final Long demoUserId = 1L; // ⚠️ временно

    public CartView(CartService cartService) {
        this.cartService = cartService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("🛒 Cart"));

        grid.removeAllColumns();
        grid.addColumn(CartResponse.CartItemDto::getName).setHeader("Product");
        grid.addColumn(CartResponse.CartItemDto::getQuantity).setHeader("Quantity");
        grid.addColumn(CartResponse.CartItemDto::getPrice).setHeader("Price");

        grid.addComponentColumn(item ->
                new Button("Удалить", e -> {
                    cartService.removeItem(demoUserId, item.getProductId());
                    loadCart();
                })
        ).setHeader("Actions");

        add(grid, totalPrice);

        Button refreshBtn = new Button("Обновить", e -> loadCart());
        Button clearBtn = new Button("Очистить корзину", e -> clearCart());

        HorizontalLayout actions = new HorizontalLayout(refreshBtn, clearBtn);
        add(actions);

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
