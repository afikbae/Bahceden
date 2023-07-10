package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Address;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressesApi {

    /**
     * Retrieves the address (if it exists) from
     * the backend with the given id
     *
     * @param addressId id of the address
     * @return the address
     */
    @GET("addresses/{addressId}")
    Call<Address> getAddressById(@Path("addressId") int addressId);

    /**
     * Deletes the address (if it exists) from
     * the backend with the given id
     *
     * @param addressId id of the address
     * @return a success message if the deletion was successful
     */
    @DELETE("addresses/{addressId}")
    Call<String> deleteAddressById(@Path("addressId") int addressId);

    /**
     * Updates the address in the database
     *
     * @param address the address to update the
     *                database with
     * @return the updated address
     */
    @PUT("addresses")
    Call<Address> updateAddress(@Body Address address);


    /**
     * Saves a new address to the database
     *
     * @param address address to be saved
     * @return the saved address
     */
    @POST("addresses")
    Call<Address> saveAddress(@Body Address address);
}
