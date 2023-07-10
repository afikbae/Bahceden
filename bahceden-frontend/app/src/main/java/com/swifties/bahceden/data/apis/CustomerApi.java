package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Customer;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CustomerApi {
    /**
     * Retrieves all the customers in the
     * database
     *
     * @return A list containing all the
     * customers
     */
    @GET("customers")
    Call<List<Customer>> getAllCustomers();

    /**
     * Retrieves a customer from the backend
     * with the given id (if it exists)
     *
     * @param id The id of the customer
     * @return The customer with the given id
     */
    @GET("customers/{customerId}")
    Call<Customer> getCustomerById(@Path("customerId") int id);

    /**
     * retrieve customer according to the email for the log in purposes
     * @param email
     * @return customer with given email
     */
    @GET("customers/email")
    Call<Customer> getCustomerByEmail(@Query("email") String email);

    /**
     * Saves a new customer to the database
     *
     * @param customer The new customer data
     * @return The new customer
     */
    @POST("customers")
    Call<Customer> save(@Body Customer customer);

    @POST("customers/favorites/products")
    Call<Customer> postNewFavoriteProduct(@Query("customerId") int customer_id, @Query("productId") int product_id);

    @DELETE("customers/favorites/products")
    Call<Customer> deleteNewFavoriteProduct(@Query("customerId") int customer_id, @Query("productId") int product_id);

    @POST("customers/favorites/producers")
    Call<Customer> postNewFavoriteProducer(@Query("customerId") int customer_id, @Query("producerId") int producer_id);

    @DELETE("customers/favorites/producers")
    Call<Customer> deleteNewFavoriteProducer(@Query("customerId") int customer_id, @Query("producerId") int producer_id);

    @Multipart
    @POST("customers/{customerId}/image")
    Call<Customer> uploadCustomerImage(
            @Path("customerId") int customerId,
            @Part MultipartBody.Part image
    );

    /**
     * Updates a customer's data on the backend
     * using the data given
     *
     * @param customer The updated customer data
     * @return The updated customer
     */
    @PUT("customers")
    Call<Customer> updateCustomer(@Body Customer customer);

    /**
     * Deletes a customer from the database
     *
     * @param id The id of the customer
     * @return Whether deletion attempt was successful or not
     */
    @DELETE("customer/{customerId}")
    Call<String> deleteCustomer(@Path("customerId") int id);

    @GET("customers/{customerId}/profit")
    Call<Double[]> getCustomerProfit(@Path("customerId") int customerId);

}
