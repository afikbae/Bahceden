package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CartApi {

    // FIXME: I'm not sure if this method should exist,
    //  it does exist on the backend so it will stay here for now

    /**
     * Retrieves all the products that are in the database
     *
     * @return all orders in the database
     */
    @GET("products")
    Call<List<Order>> getAllOrders();

    /**
     * TODO: This part should come from the backend. I'm not sure about the semantics
     *  we'll use to describe a request that is done by a customer or producer
     * Returns all the orders in a customer's cart. The path includes a "/c" at
     * the start as the backend doesn't distinguish between GET requests made
     * with customer and producer id's (yet).
     *
     * @param customerId id of the customer
     * @return all orders in said customer's cart
     */
    @GET("orders/c{customerId}")
    Call<List<Order>> getCartOfCustomerById(@Path("customerId") int customerId);

    /**
     * Searches through all orders that are in
     * IN_CART status and deletes the order
     * if there exists an order with
     * customer_id set to customerId and
     * product_id set to productId.
     *
     * @param orderId The id of the order
     * @return the deleted Order
     */
    @DELETE("customer/{orderId}")
    Call<Order> deleteOrderFromCart(@Path("orderId") int orderId);
}
