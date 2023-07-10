package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Customer;
import com.swifties.bahceden.Bahceden.entity.Producer;
import com.swifties.bahceden.Bahceden.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Integer> {
    @Query("SELECT p FROM Order o JOIN o.producer p WHERE o.id = :orderId")
    Producer findProducerByOrderId(@Param("orderId") int orderId);

    @Query("SELECT p FROM Product pr JOIN pr.producer p WHERE pr.id = :productId")
    Producer findProducerByProductId(@Param("productId") int productId);

    @Query("SELECT p FROM Producer p WHERE p.email = :email")
    Producer findByEmail(@Param("email") String email);

    List<Producer> findByShopNameContaining(String keyword);

    List<Producer> findByNameContaining(String keyword);

    List<Producer> findByNameContaining(String keyword, Sort sort);
}
