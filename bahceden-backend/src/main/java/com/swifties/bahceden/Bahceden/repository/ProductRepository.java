package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Address;
import com.swifties.bahceden.Bahceden.entity.Producer;
import com.swifties.bahceden.Bahceden.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Order o JOIN o.product p WHERE o.id = :orderId")
    Product findProductByOrderId(@Param("orderId") int orderId);

    @Query("SELECT p FROM Comment c JOIN c.product p WHERE c.id = :commentId")
    Product findProductByCommentId(@Param("commentId") int commentId);

    List<Product> findByNameContaining(String keyword);

    List<Product> findByNameContaining(String keyword, Sort sort);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :categoryId AND p.id != :productId")
    List<Product> findByCategory(@Param("categoryId") int categoryId, @Param("productId") int productId);

    @Query("SELECT p FROM Product p JOIN p.category c WHERE c.id = :categoryId")
    List<Product> findByCategoryAll(@Param("categoryId") int categoryId);

    @Query("SELECT p FROM Product p WHERE p.subCategory.id = :subCategoryId")
    List<Product> findBySubCategoryAll(@Param("subCategoryId") int subCategoryId);

    @Query("SELECT p FROM Product p WHERE p.producer.id = :producerId")
    List<Product> findByProducerId(@Param("producerId") int producerId);

}
