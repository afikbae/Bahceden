package com.swifties.bahceden.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.ProductListingAdapter;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.ProductApi;
import com.swifties.bahceden.databinding.ActivityCategoryItemsBinding;
import com.swifties.bahceden.models.Product;
import com.swifties.bahceden.uiclasses.GridSpacingItemDecoration;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryItemsActivity extends AppCompatActivity {
    ActivityCategoryItemsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoryItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        if (!intent.hasExtra("category_id"))
            super.onBackPressed();

        binding.backButton.setOnClickListener(v -> super.onBackPressed());

        RetrofitService.getApi(ProductApi.class).getProductByCategoryId(intent.getIntExtra("category_id", -1)).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                List<Product> products = response.body();

                int spanCount = 2;
                int dp_spacing = 30;
                int spacing = Math.round(dp_spacing * getResources().getDisplayMetrics().density);
                boolean includeEdge = false;
                binding.categoryItemsRV.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
                binding.categoryItemsRV.setHasFixedSize(true);
                binding.categoryItemsRV.setLayoutManager(new GridLayoutManager(CategoryItemsActivity.this, 2));
                binding.categoryItemsRV.setAdapter(new ProductListingAdapter(response.body(), CategoryItemsActivity.this, getLayoutInflater()));
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                throw new RuntimeException(t);
            }
        });
    }
}