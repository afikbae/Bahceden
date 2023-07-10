package com.swifties.bahceden.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.swifties.bahceden.R;
import com.swifties.bahceden.adapters.YourListingsAdapter;
import com.swifties.bahceden.data.AuthUser;
import com.swifties.bahceden.data.RetrofitService;
import com.swifties.bahceden.data.apis.ProducerApi;
import com.swifties.bahceden.databinding.ActivityProducerAllProductsBinding;
import com.swifties.bahceden.models.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProducerAllProductsActivity extends AppCompatActivity {

    private ActivityProducerAllProductsBinding binding;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProducerAllProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.producerAllProductsRV.setHasFixedSize(true);
        binding.producerAllProductsRV.setLayoutManager(new LinearLayoutManager(ProducerAllProductsActivity.this));
        binding.producerProductsBackButton.setOnClickListener(v -> {
            ProducerAllProductsActivity.super.onBackPressed();
            finish();
        });

        RetrofitService.getApi(ProducerApi.class).getProductsOfProducer(AuthUser.getProducer().getId()).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.body() != null) {
                    products = response.body();
                    binding.producerAllProductsRV.setAdapter(new YourListingsAdapter(products, ProducerAllProductsActivity.this));
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Toast.makeText(ProducerAllProductsActivity.this, "PROBLEM", Toast.LENGTH_SHORT).show();
            }
        });
    }
}