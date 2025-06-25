package org.skypro.counter.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skypro.counter.model.article.Article;
import org.skypro.counter.model.product.Product;
import org.skypro.counter.model.product.SimpleProduct;
import org.skypro.counter.model.search.SearchResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchServiceTest {

    @Mock
    private StorageService storageService;

    @InjectMocks
    private SearchService searchService;

    @Test
    void search_shouldReturnEmptyListWhenNoItems() {
        when(storageService.getAllSearchables()).thenReturn(Collections.emptyList());
        assertTrue(searchService.search("test").isEmpty());
    }

    @Test
    void search_shouldReturnEmptyListWhenNoMatches() {
        Product product = createTestProduct("Laptop");
        Article article = createTestArticle("Review", "Best laptops of 2023");

        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(product, article));

        assertTrue(searchService.search("smartphone").isEmpty());
    }

    @Test
    void search_shouldReturnMatchingItems() {

        Product product = createTestProduct("Smartphone");
        Article article = createTestArticle("Laptop Review", "Best laptops of 2023");
        when(storageService.getAllSearchables())
                .thenReturn(Arrays.asList(product, article));
        Collection<SearchResult> results = searchService.search("laptop");

        assertEquals(1, results.size());

        SearchResult foundResult = results.stream().filter(r -> r.getName().equals("Laptop Review")).findFirst().orElse(null);

        assertNotNull(foundResult);
        assertEquals("Laptop Review", foundResult.getName());
    }

    public Product createTestProduct(String name) {
        return new SimpleProduct(UUID.randomUUID(), name, 1000) {
            @Override
            public int getPrice() {
                return 1000;
            }

            @Override
            public boolean isSpecial() {
                return false;
            }
        };
    }

    private Article createTestArticle(String title, String text) {
        return new Article(UUID.randomUUID(), title, text);
    }
}