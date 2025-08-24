package com.swadlok.utility;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@AllArgsConstructor
public enum ImageCategory {

    PRODUCT("products", Paths.get("uploads/products")),
    USER("users", Paths.get("uploads/users")),
    CATEGORY("categories", Paths.get("uploads/categories")),
    ORDER("orders", Paths.get("uploads/orders")),
    GENERAL("general", Paths.get("uploads/general"));

    private final String folderName;
    private final Path directory;

    public static ImageCategory fromString(String value) {
        for (ImageCategory category : ImageCategory.values()) {
            if (category.name().equalsIgnoreCase(value)) {
                return category;
            }
        }
        throw new IllegalArgumentException("Invalid category: " + value);
    }
}
