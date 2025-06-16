package org.skypro.counter.service;

import org.skypro.counter.model.basket.BasketItem;
import org.skypro.counter.model.basket.ProductBasket;
import org.skypro.counter.model.basket.UserBasket;
import org.skypro.counter.model.product.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class BasketService {
    private final ProductBasket productBasket;
    private final StorageService storageService;

    public BasketService(ProductBasket productBasket, StorageService storageService) {
        this.productBasket = productBasket;
        this.storageService = storageService;
    }

    public void addProduct(UUID productId) {
        Optional<Product> product = storageService.getProductById(productId);
        if (product.isEmpty()) {
            throw new IllegalArgumentException("Продукт не найден");
        }
        productBasket.addProduct(productId);
    }

    public UserBasket getUserBasket() {
        Map<UUID, Integer> items = productBasket.getItems();
        List<BasketItem> basketItems = new ArrayList<>();

        for (Map.Entry<UUID, Integer> entry : items.entrySet()) {
            UUID productId = entry.getKey();
            int quantity = entry.getValue();
            Product product = storageService.getProductById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Продукт не найден: " + productId));
            basketItems.add(new BasketItem(product, quantity));
        }

        return new UserBasket(basketItems);
    }
}