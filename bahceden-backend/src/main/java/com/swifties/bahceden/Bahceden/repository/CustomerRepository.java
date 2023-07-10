package com.swifties.bahceden.Bahceden.repository;

import com.swifties.bahceden.Bahceden.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Order o JOIN o.customer c WHERE o.id = :orderId")
    Customer findCustomerByOrderId(@Param("orderId") int orderId);

    @Query("SELECT c FROM Comment cm JOIN cm.author c WHERE cm.id = :commentId")
    Customer findCustomerByCommentId(@Param("commentId") int commentId);

    @Query("SELECT c FROM Customer c WHERE c.email = :email")
    Customer findByEmail(@Param("email") String email);
}
