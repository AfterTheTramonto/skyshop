package org.skypro.counter.service;

import org.skypro.counter.model.article.Article;
import org.skypro.counter.model.product.*;
import org.skypro.counter.model.search.Searchable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StorageService {
    private final Map<UUID, Product> products = new ConcurrentHashMap<>();
    private final Map<UUID, Article> articles = new ConcurrentHashMap<>();

    public StorageService() {
        initTestData();
    }

    private void initTestData() {
        // Добавляем тестовые продукты
        addProduct(new SimpleProduct(UUID.randomUUID(), "Ноутбук Lenovo", 45000));
        addProduct(new DiscountedProduct(UUID.randomUUID(), "Смартфон Samsung", 30000, 15));
        addProduct(new FixPriceProduct(UUID.randomUUID(), "Чехол для телефона"));

        // Добавляем тестовые статьи
        addArticle(new Article(UUID.randomUUID(), "Обзор ноутбуков", "Лучшие ноутбуки 2023 года..."));
        addArticle(new Article(UUID.randomUUID(), "Как выбрать смартфон", "Руководство по выбору..."));
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public void addArticle(Article article) {
        articles.put(article.getId(), article);
    }

    public Collection<Product> getAllProducts() {
        return products.values();
    }

    public Collection<Article> getAllArticles() {
        return articles.values();
    }

    public Collection<Searchable> getAllSearchables() {
        List<Searchable> result = new ArrayList<>();
        result.addAll(products.values());
        result.addAll(articles.values());
        return result;
    }
}