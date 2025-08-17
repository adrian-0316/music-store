package com.example.music_store.ui;

import com.example.music_store.dto.ProductResponse;
import com.example.music_store.service.ProductService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Route("products")  // UI будет доступен по http://localhost:8080/products
@Component          // важно: теперь Spring будет управлять этим классом
public class ProductView extends VerticalLayout {

    private final ProductService productService;

    @Autowired
    public ProductView(ProductService productService) {
        this.productService = productService;

        add(new H1("Музыкальные инструменты"));

        // Таблица продуктов
        Grid<ProductResponse> grid = new Grid<>(ProductResponse.class, false);
        grid.addColumn(ProductResponse::getName).setHeader("Название");
        grid.addColumn(ProductResponse::getCategory).setHeader("Категория");
        grid.addColumn(ProductResponse::getPrice).setHeader("Цена");
        grid.addColumn(ProductResponse::getDescription).setHeader("Описание");

        //"В корзину"
        grid.addComponentColumn(product ->
                new Button("Добавить в корзину", click -> {
                    // Здесь пока просто лог
                    System.out.println("Добавлен в корзину: " + product.getName());
                    // Позже свяжем с CartService
                })
        );

        //Данные
        grid.setItems(productService.findAll());

        add(grid);
    }
}
