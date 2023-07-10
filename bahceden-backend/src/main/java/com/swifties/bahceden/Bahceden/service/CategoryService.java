package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> findAll();

    Category findById(int theId);

    Category findCategoryByProductId(int productId);

    Category save(Category theCategory);

    void deleteById(int theId);
}
