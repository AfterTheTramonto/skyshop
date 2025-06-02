package org.skypro.counter.model.product;

import java.util.UUID;

public class SimpleProduct extends Product {
    private final int price;

    public SimpleProduct(UUID id, String name, int price) {
        super(id, name);
        if (price <= 0) {
            throw new IllegalArgumentException("Price  must be greater than 0");
        }
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public boolean isSpecial() {
        return false;
    }
}