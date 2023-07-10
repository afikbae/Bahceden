package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Product pr JOIN pr.category c WHERE pr.id = :productId")
    Category findCategoryByProductId(@Param("productId") int productId);

    @Query("SELECT c FROM Category c WHERE c.parent != null")
    List<Category> findAllSubCategories();
}
