package org.skypro.counter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.counter.exception.NoSuchProductException;
import org.skypro.counter.model.basket.BasketItem;
import org.skypro.counter.model.basket.ProductBasket;
import org.skypro.counter.model.basket.UserBasket;
import org.skypro.counter.model.product.Product;
import org.skypro.counter.model.product.SimpleProduct;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceTest {

    @Mock
    private ProductBasket productBasket;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private BasketService basketService;

    @Test
    void addProduct_shouldThrowExceptionWhenProductNotFound() {

        UUID productId = UUID.randomUUID();
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());


        assertThrows(NoSuchProductException.class, () -> {
            basketService.addProduct(productId);
        });

        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void addProduct_shouldAddToBasketWhenProductExists() {

        UUID productId = UUID.randomUUID();
        Product product = new SimpleProduct(productId, "Test Product", 100);

        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));


        basketService.addProduct(productId);


        verify(productBasket, times(1)).addProduct(productId);
    }

    @Test
    void getUserBasket_shouldReturnEmptyBasketWhenNoItems() {

        when(productBasket.getItems()).thenReturn(Collections.emptyMap());


        UserBasket result = basketService.getUserBasket();


        assertTrue(result.getItems().isEmpty());
        assertEquals(0, result.getTotal());
    }

    @Test
    void getUserBasket_shouldReturnCorrectBasketWithItems() {

        UUID productId1 = UUID.randomUUID();
        UUID productId2 = UUID.randomUUID();

        Product product1 = new SimpleProduct(productId1, "Product 1", 100);
        Product product2 = new SimpleProduct(productId2, "Product 2", 200);

        Map<UUID, Integer> basketItems = Map.of(
                productId1, 2,
                productId2, 1
        );

        when(productBasket.getItems()).thenReturn(basketItems);
        when(storageService.getProductById(productId1)).thenReturn(Optional.of(product1));
        when(storageService.getProductById(productId2)).thenReturn(Optional.of(product2));


        UserBasket result = basketService.getUserBasket();


        List<BasketItem> items = result.getItems();
        assertEquals(2, items.size());

        BasketItem item1 = findItemById(items, productId1);
        assertNotNull(item1);
        assertEquals(2, item1.getQuantity());
        assertEquals(100, item1.getProduct().getPrice());

        BasketItem item2 = findItemById(items, productId2);
        assertNotNull(item2);
        assertEquals(1, item2.getQuantity());
        assertEquals(200, item2.getProduct().getPrice());

        assertEquals(400, result.getTotal()); // 100*2 + 200*1 = 400
    }


    @Test
    void getUserBasket_shouldThrowExceptionWhenProductNotFound() {

        UUID productId = UUID.randomUUID();
        Map<UUID, Integer> basketItems = Map.of(productId, 1);

        when(productBasket.getItems()).thenReturn(basketItems);
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());


        assertThrows(NoSuchProductException.class, () -> {
            basketService.getUserBasket();
        });
    }

    @Test
    void getUserBasket_shouldHandleMultipleQuantities() {

        UUID productId = UUID.randomUUID();
        Product product = new SimpleProduct(productId, "Test Product", 50);

        Map<UUID, Integer> basketItems = Map.of(productId, 5);

        when(productBasket.getItems()).thenReturn(basketItems);
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));


        UserBasket result = basketService.getUserBasket();


        assertEquals(1, result.getItems().size());
        BasketItem item = result.getItems().get(0);
        assertEquals(5, item.getQuantity());
        assertEquals(250, result.getTotal()); // 50 * 5 = 250
    }

    private BasketItem findItemById(List<BasketItem> items, UUID productId) {
        return items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
    }
}