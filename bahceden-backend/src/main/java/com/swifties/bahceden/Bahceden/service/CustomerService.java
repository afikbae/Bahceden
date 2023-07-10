package com.swifties.bahceden.Bahceden.service;

import com.swifties.bahceden.Bahceden.entity.Customer;
import com.swifties.bahceden.Bahceden.entity.FavoriteProducer;
import com.swifties.bahceden.Bahceden.entity.FavoriteProduct;

import java.util.List;

public interface CustomerService {
    List<Customer> findAll();

    Customer findById(int theId);

    Customer findByEmail(String email);

    Customer findCustomerByOrderId(int orderId);

    Customer findCustomerByCommentId(int comment);

    Customer save(Customer theCustomer);

    void deleteById(int theId);

    FavoriteProduct addFavoriteProduct(int customerId, int productId);

    String removeFavoriteProduct(int customerId, int productId);

    FavoriteProducer addFavoriteProducer(int customerId, int producerId);

    String removeFavoriteProducer(int customerId, int producerId);

    void addLike(int customerId, int commentId);

    void removeLike(int customerId, int commentId);

    boolean getLike(int customerId, int commentId);
}
