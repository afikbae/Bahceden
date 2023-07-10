package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.DTO.ProductDTO;
import com.swifties.bahceden.Bahceden.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(int theId);

    ProductDTO findCustomById(int theId);

    Product findProductByOrderId(int orderId);

    Product findProductByCommentId(int commentId);

    List<Product> findByNameContaining(String keyword);

    List<Product> findByNameContaining(String keyword, Sort sort);

    Product save(Product theProduct);

    Page<Product> findAll(Pageable pageable);

    void deleteById(int theId);

    List<Product> findSimilarProducts(int productId);
    List<Product> findCategoryProducts(int categoryId);
}
