package com.swifties.bahceden.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swifties.bahceden.databinding.ActivityCustomerAddCardBinding;

public class CustomerAddCardActivity extends AppCompatActivity {
    private ActivityCustomerAddCardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerAddCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Toast.makeText(this, "AFAIK this page won't exist", Toast.LENGTH_SHORT).show();

        binding.customerAddCardBackButton.setOnClickListener(backView -> CustomerAddCardActivity.super.onBackPressed());
    }
}