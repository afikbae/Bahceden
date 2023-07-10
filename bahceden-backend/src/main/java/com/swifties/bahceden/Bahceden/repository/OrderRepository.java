package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Category;
import com.swifties.bahceden.Bahceden.entity.FavoriteProduct;
import com.swifties.bahceden.Bahceden.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE o.producer.id = :producerId")
    List<Order> findProducerOrders(@Param("producerId") int producerId);

    List<Order> findByCustomerId(Integer customerId);

    @Query("SELECT o FROM Order o WHERE o.producer.id = :producerId")
    List<Order> findOrders(@Param("producerId") int producerId);

    List<Order> findByStatusNotIn(List<Integer> statuses);
}
