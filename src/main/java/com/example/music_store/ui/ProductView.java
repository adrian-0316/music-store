package com.example.music_store.ui;

import com.example.music_store.dto.ProductResponse;
import com.example.music_store.service.ProductService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Route("products") // URL: http://localhost:8080/products
@RequiredArgsConstructor
public class ProductView extends VerticalLayout {

    private final ProductService productService;

    private final Grid<ProductResponse> grid = new Grid<>(ProductResponse.class);

    public ProductView() {
        setSizeFull();
        add(grid);
    }

    // Vaadin вызывает этот метод при переходе на /products
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        List<ProductResponse> products = productService.findAll();
        grid.setItems(products);

        grid.setColumns("id", "name", "description", "price", "category");
        grid.getColumnByKey("id").setHeader("ID");
        grid.getColumnByKey("name").setHeader("Название");
        grid.getColumnByKey("description").setHeader("Описание");
        grid.getColumnByKey("price").setHeader("Цена");
        grid.getColumnByKey("category").setHeader("Категория");
    }
}
