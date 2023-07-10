package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Producer;
import com.swifties.bahceden.models.ProducerDataDTO;
import com.swifties.bahceden.models.Product;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProducerApi {

    /**
     * Retrieves all the producers in
     * the database
     *
     * @return a list of all the producers
     * the database
     */
    @GET("producers")
    Call<List<Producer>> getAllProducers();

    /**
     * Retrieves (if it exists) a producer from
     * the database with the given id
     *
     * @param producerId the producer's id
     * @return a producer instance
     */
    @GET("producers/{producerId}")
    Call<Producer> getProducerById(@Path("producerId") int producerId);

    /**
     * retrieve producer according to the email for the log in purposes
     *
     * @param email
     * @return producer with given email
     */
    @GET("producers/email")
    Call<Producer> getProducerByEmail(@Query("email") String email);

    /**
     * Retrieves all the producers that
     * contain a given keyword in their
     * name
     *
     * @param keyword the given keyword
     * @return all producers that contain
     * the keyword in their name
     */
    @GET("producers/searchParam")
    Call<List<Producer>> searchProducer(@Query("keyword") String keyword, @Query("sortBy") String sortBy, @Query("ascending") boolean isAscending);

    /**
     * Saves a new producer onto the database
     *
     * @param producer the producer that will be saved
     * @return a copy of the producer that was saved
     */
    @POST("producers")
    Call<Producer> save(@Body Producer producer);

    /**
     * Updates a producer in the database with
     * the given information
     *
     * @param producer the updated producer object
     * @return a copy of the producer that was updated
     */
    @PUT("producers")
    Call<Producer> updateProducer(@Body Producer producer);

    @Multipart
    @PUT("producers/{producersId}/image")
    Call<Producer> uploadImage(
            @Path("producersId") int producerId,
            @Part MultipartBody.Part image,
            @Query("type") String type
    );

    @GET("producers/{producerId}/products")
    Call<List<Product>> getProductsOfProducer(@Path("producerId") int producerId);

    @GET("producers/data")
    Call<ProducerDataDTO> getProductData(@Query("subCategory") int categoryId);
}
