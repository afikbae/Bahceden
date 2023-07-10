package com.swifties.bahceden.data.apis;

import com.swifties.bahceden.models.Category;
import com.swifties.bahceden.models.Customer;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryApi {

    @GET("categories")
    Call<List<Category>> getAllCategories();
}
