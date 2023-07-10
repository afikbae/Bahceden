package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Product;

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

public interface ProductApi {
    @GET("products")
    Call<List<Product>> getAllProducts();

    /**
     * Retrieves a list of products that
     * correspond to a given keyword
     *
     * @param keyword keyword to be searched for
     * @return a list of products
     */
    @GET("products/searchParam")
    Call<List<Product>> searchProduct(@Query("keyword") String keyword, @Query("sortBy") String sortBy, @Query("ascending") boolean isAscending);

    /**
     * Retrieves a single product from the backend
     *
     * @param id the desired product's id
     * @return the desired product
     */
    @GET("products/{productId}")
    Call<Product> getProductById(@Path("productId") int id);
    @GET("products/custom/{productId}")
    Call<Product> getProductByIdWithDetailedComments(@Path("productId") int id);

    /**
     * Deletes the product from the database
     *
     * @param id the id of the product
     * @return whether deletion was successful or not
     */
    @DELETE("products/{productId}")
    Call<String> deleteProductById(@Path("productId") int id);

    /**
     * Saves a product to the database
     * @param product the product to be saved
     * @return a copy of the saved product
     */
    @POST("products")
    Call<Product> saveProduct(@Body Product product);

    @PUT("products")
    Call<Product> updateProduct(@Body Product product);

    @GET("/products/newArrivals")
    Call<List<Product>> getNewArrivals();

    @GET("products/similar")
    Call<List<Product>> getSimilarProducts(@Query("product") int productId);

    @GET("categories/products")
    Call<List<Product>> getProductByCategoryId(@Query("category") int categoryId);

    @Multipart
    @POST("products/{productId}/image")
    Call<Product> uploadProductImage(
            @Path("productId") int productId,
            @Part MultipartBody.Part image
    );

    @GET("products/populars")
    Call<List<Product>> getPopularProducts();
}
