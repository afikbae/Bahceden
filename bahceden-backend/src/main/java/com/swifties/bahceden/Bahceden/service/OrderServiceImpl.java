package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Order;
import com.swifties.bahceden.Bahceden.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository theOrderRepository){
        orderRepository = theOrderRepository;
    }
    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(int theId) {
        Optional<Order> result = orderRepository.findById(theId);

        Order theOrder = null;

        if(result.isPresent()){
            theOrder = result.get();
        }
        else{
            throw new RuntimeException("Did not find order id - " + theId);
        }

        return theOrder;
    }

    @Override
    public Order save(Order theOrder) {
        return orderRepository.save(theOrder);
    }

    @Override
    public List<Order> getCompletedOrders() {
        List<Integer> excludedStatuses = Arrays.asList(1, 5);
        return orderRepository.findByStatusNotIn(excludedStatuses);
    }

    @Override
    public List<Order> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    @Override
    public void deleteById(int theId) {
        orderRepository.deleteById(theId);
    }
}
