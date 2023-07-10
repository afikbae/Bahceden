package com.swifties.bahceden.Bahceden.rest;

import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    private CategoryController(CategoryService theCategoryService){
        categoryService = theCategoryService;
    }

    @GetMapping("/categories")
    public List<Category> findAll(){
        return categoryService.findAll();
    }

    @GetMapping("/categories/{categoryId}")
    public Category getCategory(@PathVariable int categoryId){

        Category theCategory = categoryService.findById(categoryId);

        if(theCategory == null){
            throw new RuntimeException("Category id did not found - " + categoryId);
        }

        return theCategory;
    }

    @PostMapping("/categories")
    public Category addCategory(@RequestBody Category theCategory){

        // also just in case they pass an id in JSON ... set id to 0
        // this is to force a save of new item ... instead of update

        theCategory.setId(0);

        Category dbCategory = categoryService.save(theCategory);

        return dbCategory;
    }

    @PutMapping("/categories")
    public Category updateCategory(@RequestBody Category theCategory){

        Category dbCategory = categoryService.save(theCategory);

        return dbCategory;
    }

    @DeleteMapping("/categories/{categoryId}")
    public String deleteCategory(@PathVariable int categoryId){

        Category tempCategory = categoryService.findById(categoryId);

        // throw exception if null

        if(tempCategory == null){
            throw new RuntimeException("Category id not found - " + categoryId);
        }

        categoryService.deleteById(categoryId);

        return "Deleted category id - " + categoryId;
    }

    @GetMapping("/products/{productId}/category")
    public Category getCategoryByProductId(@PathVariable int productId) {
        Category category = categoryService.findCategoryByProductId(productId);

        if (category == null) {
            throw new RuntimeException("No category found for product ID - " + productId);
        }

        return category;
    }

}
