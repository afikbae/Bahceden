package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Order;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderService {
    List<Order> findAll();

    Order findById(int theId);

    Order save(Order theOrder);

    void deleteById(int theId);

    List<Order> getCompletedOrders();

    List<Order> getOrdersByCustomerId(Integer customerId);

}
