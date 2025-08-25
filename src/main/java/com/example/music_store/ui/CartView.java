package com.example.music_store.ui;

import com.example.music_store.dto.CartResponse;
import com.example.music_store.service.CartService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;

@Route("cart")
@RequiredArgsConstructor
public class CartView extends VerticalLayout {

    private final CartService cartService;

    private final Grid<CartResponse.CartItemDto> cartGrid = new Grid<>(CartResponse.CartItemDto.class);
    private final Span totalPriceLabel = new Span();

    // ⚠️ Пока userId захардкодим, потом свяжем с Security
    private final Long userId = 1L;

    public CartView(CartService cartService) {
        this.cartService = cartService;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Корзина"));

        cartGrid.setColumns("name", "quantity", "price");
        cartGrid.addComponentColumn(item ->
                new Button("Удалить", e -> removeItem(item.getProductId()))
        );

        add(cartGrid, totalPriceLabel);

        loadCart();
    }

    private void loadCart() {
        CartResponse cart = cartService.getCart(userId);
        cartGrid.setItems(cart.getItems());
        totalPriceLabel.setText("Итого: " + cart.getTotalPrice() + " ₽");
    }

    private void removeItem(Long productId) {
        cartService.removeItem(userId, productId);
        loadCart();
    }
}
