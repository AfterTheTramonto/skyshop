package org.skypro.counter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.counter.exception.NoSuchProductException;
import org.skypro.counter.model.basket.ProductBasket;
import org.skypro.counter.model.basket.UserBasket;
import org.skypro.counter.model.product.SimpleProduct;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
        // Подготовка
        UUID productId = UUID.randomUUID();
        when(storageService.getProductById(productId)).thenReturn(Optional.empty());

        // Действие + Проверка
        assertThrows(NoSuchProductException.class, () ->
                basketService.addProduct(productId)
        );

        // Проверка взаимодействий
        verify(productBasket, never()).addProduct(any());
    }

    @Test
    void getUserBasket_shouldReturnCorrectBasket() {
        // Подготовка
        UUID productId = UUID.randomUUID();
        SimpleProduct product = new SimpleProduct(productId, "Test Product", 100);

        when(productBasket.getItems()).thenReturn(Map.of(productId, 2));
        when(storageService.getProductById(productId)).thenReturn(Optional.of(product));

        // Действие
        UserBasket result = basketService.getUserBasket();

        // Проверка
        assertEquals(1, result.getItems().size());
        assertEquals(200, result.getTotal());
    }
}