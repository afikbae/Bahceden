package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository theCategoryRepository){
        categoryRepository = theCategoryRepository;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(int theId) {
        Optional<Category> result = categoryRepository.findById(theId);

        Category theCategory = null;

        if(result.isPresent()){
            theCategory = result.get();
        }
        else{
            throw new RuntimeException("Did not find employee id - " + theId);
        }

        return theCategory;
    }

    @Override
    public Category findCategoryByProductId(int theProductId) {
        return categoryRepository.findCategoryByProductId(theProductId);
    }

    @Override
    public Category save(Category theCategory) {
        return categoryRepository.save(theCategory);
    }

    @Override
    public void deleteById(int theId) {
        categoryRepository.deleteById(theId);
    }
}
