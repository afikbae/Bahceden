package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface OrderApi {

    /**
     * Retrieves all products in the database
     *
     * @return All orders in the database
     */
    @GET("orders")
    Call<List<Order>> getAllOrders();

    /**
     * Retrieves order by id
     *
     * @param orderId
     * @return An order by id
     */
    @GET("orders/{orderId}")
    Call<Order> getOrderByID(@Path("orderId") int orderId);

    @POST("orders")
    Call<Order> postOrder(@Body Order order);

    @PUT("orders/{orderId}")
    Call<Order> putOrder(@Body Order order, @Path("orderId") int orderId);

    /**
     * Deletes an order from the database
     *
     * @param orderId the order's id
     * @return A message indicating whether deletion attempt was successful
     * @apiNote This will be used to delete an item from a cart
     * and nowhere else. All orders that are out of a customer's cart should stay in the database
     */
    @DELETE("orders/{orderId}")
    Call<Order> deleteById(@Path("orderId") int orderId);

    @GET("producers/{producerId}/orders")
    Call<List<Order>> getOrdersOfProducer(@Path("producerId") int producerId);
}
