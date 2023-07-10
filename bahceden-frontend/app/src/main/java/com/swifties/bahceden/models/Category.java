package com.swifties.bahceden.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Category {
    private static List<Category> existingCategories;

    private int id;
    private String name;
    private int parentId;

    private Category () {}

    private Category(int id, String name, int parentId) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

    public static Category getCategory(int id, String name, int parentId)
    {
        if (existingCategories == null)
        {
            existingCategories = new ArrayList<>();
        }
        Optional<Category> optionalCategory = existingCategories.stream()
                .filter(c -> c.getId() == id)
                .findFirst();

        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            if (!category.getName().equals(name)) {
                throw new RuntimeException("category id's match but names aren't!");
            }
            return category;
        }

        Category category = new Category(id, name, parentId);
        existingCategories.add(category);
        return category;
    }

    public static Category getCategory(Category category)
    {
        return getCategory(category.getId(), category.getName(), category.getParentId());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getParentId() {
        return parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
